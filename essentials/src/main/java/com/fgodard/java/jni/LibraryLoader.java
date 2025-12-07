package com.fgodard.java.jni;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class LibraryLoader {

	private static final Map<String,String> loadedLibs = new HashMap<>();

	// private constructor
	private LibraryLoader() {
	}

	public static synchronized void load(final String libName, final String libPath) throws LibraryNotFoundException {

        if (libName == null) {
            return;
        }

        final String name = libName.trim();
        if (name.isEmpty()) {
            return;
        }

        System.out.print("chargement de ");
        System.out.print(name);
        System.out.print(", arch=");
        System.out.println(getSysLibraryPath());

		loadLibrary(name, libPath);
	}

    private static void loadLibrary(final String libName, final String path) throws LibraryNotFoundException {

        try {

            if ("system".equals(path)) {
                final String finalPath = "system/"+libName;
                if (loadedLibs.values().contains(finalPath)) {
                    // déjà chargée
                    return;
                }
                System.loadLibrary(libName);
                loadedLibs.put(libName, finalPath);

            } else {
                String foundPath = translateLibPath(libName, path).toString();
                if (loadedLibs.values().contains(foundPath)) {
                    // déjà chargée sous un autre id
                    return;
                }
                System.load(foundPath);
                loadedLibs.put(libName, foundPath);
            }

        } catch (LibraryNotFoundException e) {
            throw(e);

        } catch (Exception | UnsatisfiedLinkError e) {
            throw new LibraryNotFoundException(e, "Impossible de charger %s.", libName);

        }

    }

    private static String getSysLibraryPath() {
        final String arch = System.getProperty("os.arch").toLowerCase().trim();
        String result;
        if ("arm".equals(arch)) {
            result = "/lib/armhf/";
        } else if ("arm64".equals(arch)) {
            result = "/lib/aarch64/";
        } else {
            result = "/lib/" + arch + "/";
        }
        return result;
    }

	private static Path translateLibPath(final String libName, final String libPath) throws LibraryNotFoundException {

        Collection<Throwable> exceptionList = new ArrayList<>();

        Path resultPath;
        try {
            resultPath = Paths.get(libPath);
            if (Files.exists(resultPath)) {
                return resultPath.toAbsolutePath();
            } else {
                exceptionList.add(new FileNotFoundException(resultPath.toAbsolutePath() + " not present."));
            }

        } catch (InvalidPathException e) {
            exceptionList.add(e);
        }

        final String sysLibPath = getSysLibraryPath();
        final String resourcePath = sysLibPath.substring(1) + libPath;
        try {
            // chemin relatif : recherche à l'intérieur du .jar
            URL libUrl = LibraryLoader.class.getClassLoader().getResource(resourcePath);
            if (libUrl == null) {
                exceptionList.add(new FileNotFoundException(sysLibPath+libPath + " not found in resources."));

            } else {
                Path tmpPath = buildResourcePath(resourcePath, exceptionList);
                if (tmpPath != null) return tmpPath;

            }
        } catch (Exception e) {
            exceptionList.add(e);
        }

        // pas trouvé dans le jar : recherche au niveau des rep systemes.
        try {
            resultPath = Paths.get(sysLibPath + libPath);
            if (Files.exists(resultPath)) {
                return resultPath.toAbsolutePath();

            } else {
                exceptionList.add(new FileNotFoundException(sysLibPath + libPath));

            }

        } catch (InvalidPathException e) {
            exceptionList.add(e);

        }

        throw new LibraryNotFoundException(exceptionList, "Impossible de localiser %s.", libName);

    }

    private static Path buildResourcePath(String libPath, Collection<Throwable> exceptionList) {
        int p1 = libPath.lastIndexOf('/');

        String tmpFileName;
        String tmpFileExt = null;

        if (p1>0) {
            tmpFileName = libPath.substring(p1+1);
        } else {
            tmpFileName = libPath;
        }
        int p2 = tmpFileName.lastIndexOf('.');
        if (p2>0) {
            tmpFileExt = tmpFileName.substring(p2);
            tmpFileName = tmpFileName.substring(0, p2);
        }

        InputStream libStream=null;
        try {
            Path tmpPath = Files.createTempFile(tmpFileName, tmpFileExt);
            tmpPath.toFile().deleteOnExit();

            libStream =LibraryLoader.class.getClassLoader().getResourceAsStream(libPath);
            Files.copy(libStream, tmpPath, StandardCopyOption.REPLACE_EXISTING);
            libStream.close();
            return tmpPath;

        } catch(Exception e) {
            exceptionList.add(e);

        } finally {
            if (libStream != null) {
                try {
                    libStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
