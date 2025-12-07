package com.fgodard.config;

import com.fgodard.config.exceptions.ConfigurationFileException;
import com.fgodard.config.exceptions.ConfigurationNotFoundException;
import com.fgodard.files.cache.CachedFileProvider;
import com.fgodard.files.cache.exceptions.CachedFileNotFoundException;
import com.fgodard.files.cache.exceptions.CachedFileTooLargeException;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by crios on 06/08/23.
 */
public class Configuration {

    private static File getApplicationConfigDir() throws ConfigurationNotFoundException {

        String applicationConfigPath = System.getProperty("user.dir");
        if (applicationConfigPath != null && ! applicationConfigPath.isEmpty()) {
            return new File(applicationConfigPath);
        }

        throw new ConfigurationNotFoundException("Répertoire de configuration indéfini.");

    }

    private static void initProjectConfigDir(String projectName) throws ConfigurationNotFoundException {
        File applicationConfigDir = getApplicationConfigDir();
        File projectConfigPath;
        if (projectName == null) {
            projectConfigPath = applicationConfigDir;
        } else {
            projectConfigPath = new File(applicationConfigDir, projectName);
        }
        if (!projectConfigPath.exists()) {
            projectConfigPath.mkdirs();
        }
    }

    /**
     * Ecrit un fichier de config de type text au niveau global
     *
     * @param fileName
     *            : nom du fichier de config
     *
     * @throws ConfigurationNotFoundException
     * @throws ConfigurationFileException
     */
    public static <L extends Collection<String> & Serializable> void writeConfigTextFile(final String fileName, L values)
            throws ConfigurationFileException {
        File txtFile = new File(getApplicationConfigDir(), fileName);
        try (Writer writer = new BufferedWriter(new FileWriter(txtFile, false));) {
            for (String line : values) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ConfigurationFileException(e, "Impossible d'écrire le fichier de configuration %1$s", fileName);
        }
    }

    /**
     * Lit un fichier de config de type text au niveau global
     *
     * @param fileName
     *            : nom du fichier de config
     *
     * @return
     *
     * @throws ConfigurationNotFoundException
     * @throws ConfigurationFileException
     */
    public static <L extends Collection<String> & Serializable> L readConfigTextFile(final String fileName, L result)
            throws ConfigurationFileException {
        File txtFile = new File(getApplicationConfigDir(), fileName);
        if (result == null) {
            return null;
        }
        return readConfigTextFile(txtFile, result);
    }

    public static <L extends Collection<String> & Serializable> L readConfigTextFile(File propFile, L result)
            throws ConfigurationFileException {
        InputStream stream;
        try {
            stream = CachedFileProvider.getCachedInputStream(propFile, 30);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
                String line = reader.readLine();
                while (line != null) {
                    String sline = line.trim();
                    if (!sline.isEmpty()) {
                        result.add(sline);
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                throw new ConfigurationFileException(e, "Erreur de lecture du fichier %1$s.", propFile.getName());
            }
            return result;
        } catch (CachedFileTooLargeException e) {
            throw new ConfigurationFileException(e, "Fichier de configuration %1$s trop volumineux. ",
                    propFile.getName());
        } catch (CachedFileNotFoundException e) {
            throw new ConfigurationNotFoundException(e, "Fichier de configuration %1$s non trouvé. ",
                    propFile.getName());
        }
    }

    public static void createPropertiesIfNotExists(String fileName) throws ConfigurationNotFoundException {
        File directory = getApplicationConfigDir();
        File propFile = new File(directory, fileName);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!propFile.exists()) {
            try {
                Files.createFile(propFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Properties readExternalProperties(String fileName, Map<String, String> values) throws ConfigurationFileException {

        File directory = getApplicationConfigDir();
        File propFile = new File(directory, fileName);
        InputStream propertiesStream;
        try {
            propertiesStream = CachedFileProvider.getCachedInputStream(propFile, 30);
            return getPropertiesFromStream(fileName, propertiesStream, "UTF-8");

        } catch (CachedFileTooLargeException e) {
            if (values == null) {
                return null;
            }
            Properties result = new Properties();
            result.putAll(values);
            return result;

        } catch (CachedFileNotFoundException e) {
            if (values == null) {
                return null;
            }

            Properties result = new Properties();
            result.putAll(values);
            try {
                writeExternalProperties(fileName, result);
            } catch (ConfigurationFileException e2) {
                e2.printStackTrace(System.err);
            }
            return result;
        }

    }

    public static Properties readExternalProperties(String fileName) throws ConfigurationFileException {
        File directory = getApplicationConfigDir();
        File propFile = new File(directory, fileName);
        InputStream propertiesStream;
        try {
            propertiesStream = CachedFileProvider.getCachedInputStream(propFile, 30);
            return getPropertiesFromStream(fileName, propertiesStream, "UTF-8");
        } catch (CachedFileTooLargeException e) {
            throw new ConfigurationFileException(e, "Fichier de configuration %1$s trop volumineux. ", fileName);
        } catch (CachedFileNotFoundException e) {
            throw new ConfigurationNotFoundException(e, "Fichier de configuration %1$s non trouvé. ", fileName);
        }
    }

    /**
     * Ecrit un fichier properties
     *
     * @throws ConfigurationNotFoundException
     * @throws ConfigurationFileException
     */
    public static void writeExternalProperties(String fileName, Map<Object, Object> values) throws ConfigurationFileException {
        File directory = getApplicationConfigDir();
        File propFile = new File(directory, fileName);
        try (Writer writer = new BufferedWriter(new FileWriter(propFile, false));) {
            List<Object> keys = new ArrayList<>();
            keys.addAll(values.keySet());
            Collections.sort(keys, (o1, o2) -> {
                final String s1 = String.valueOf(o1);
                final String s2 = String.valueOf(o2);
                return s1.compareTo(s2);
            });
            for (Object key : keys) {
                String k = String.valueOf(key).replaceAll("[ ]", "\\u0020");
                String v = String.valueOf(values.get(key)).replaceAll("[\\\\]", "\\\\");
                writer.write(k);
                writer.write("=");
                writer.write(v);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ConfigurationFileException(e, "Impossible d'écrire le fichier de configuration %1$s",
                    propFile.getAbsolutePath());
        }
    }

    private static Properties getPropertiesFromStream(String fileName, InputStream propertiesStream, String charset)
            throws ConfigurationFileException {
        try {
            // il faut convertir le stream en iso-8859-1 pour les properties
            Properties result = new Properties();
            Reader reader;
            if (charset != null && ! charset.isEmpty()) {
                reader = new InputStreamReader(propertiesStream, charset);
                result.load(reader);
            } else {
                result.load(propertiesStream);
            }
            return result;
        } catch (IOException e) {
            throw new ConfigurationFileException(e, "Fichier de configuration %1$s illisible. ", fileName);
        }
    }

    private static Properties getPropertiesFromStream(String fileName, InputStream propertiesStream) throws ConfigurationFileException {
        try {
            Properties result = new Properties();
            result.load(propertiesStream);
            return result;
        } catch (IOException e) {
            throw new ConfigurationFileException(e, "Fichier de configuration %1$s illisible. ", fileName);
        }
    }

}
