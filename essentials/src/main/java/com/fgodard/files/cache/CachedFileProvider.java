package com.fgodard.files.cache;

import com.fgodard.files.cache.exceptions.CachedFileNotFoundException;
import com.fgodard.files.cache.exceptions.CachedFileTooLargeException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cache de lecture de fichier. Thread Safe.
 */
public class CachedFileProvider {
    
    private static final CachedFileProvider INSTANCE = new CachedFileProvider();
    private static final ReentrantLock LOCK = new ReentrantLock();

    private CachedFileProvider() {

    }

    private static final long MAX_BUFFERDFILE_SIZE = 20480; // 20kO

    private final Map<String, CachedStream> cachedMap = new HashMap<>();

    private CachedStream loadFile(final File file) throws CachedFileNotFoundException, CachedFileTooLargeException {
        if (!file.exists() || !file.canRead()) {
            throw new CachedFileNotFoundException("Fichier %1$s introuvable ou inaccessible.", file.getAbsolutePath());
        }
        long size = file.length();
        if (size > MAX_BUFFERDFILE_SIZE) {
            throw new CachedFileTooLargeException("Fichier %1$s trop grand pour le cache.", file.getAbsolutePath());
        }
        try {
            Path p = file.toPath();
            long fileTime = Files.getLastModifiedTime(p).toMillis();
            CachedStream result = new CachedStream(Files.readAllBytes(p), fileTime);
            cachedMap.put(file.getAbsolutePath(), result);
            return result;

        } catch (FileNotFoundException e) {
            throw new CachedFileNotFoundException(e, "Fichier %1$s introuvable ou inaccessible.",
                    file.getAbsolutePath());

        } catch (IOException e) {
            throw new CachedFileNotFoundException(e, "Fichier %1$s illisible.", file.getAbsolutePath());

        }
    }

    /**
     *
     * @param file
     * @param stream
     * 
     * @return
     * 
     * @throws CachedFileNotFoundException
     */
    private CachedStream streamCheck(CachedStream stream, File file)
            throws CachedFileNotFoundException, CachedFileTooLargeException {
        try {
            long fileTime = Files.getLastModifiedTime(file.toPath()).toMillis();
            if (fileTime > stream.getLastModifiedTime()) {
                return loadFile(file);
            }

            stream.setLastCheckTs(System.currentTimeMillis());
            return new CachedStream(stream.getByteArray(), stream.getLastModifiedTime());

        } catch (IOException e) {
            throw new CachedFileNotFoundException(e, "Fichier %1$s illisible.", file.getAbsolutePath());

        }
    }

    private CachedStream getCachedStream(final File file, final long checkInterval)
            throws CachedFileNotFoundException, CachedFileTooLargeException {
        LOCK.lock();
        try {
            CachedStream stream = cachedMap.get(file.getAbsolutePath());
        
        if (stream == null) {
            return loadFile(file);
        }
        
        if (stream.getLastCheckTs() + checkInterval < System.currentTimeMillis()) {
            return streamCheck(stream, file);
        }
        
        return new CachedStream(stream.getByteArray(), stream.getLastModifiedTime());
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Retourne le flux du fichier mis en cache
     *
     * @param file
     *            : le fichier à lire
     * @param checkInterval
     *            : l'interval de vérification de modification de fichier (en secondes)
     * 
     * @return le flux
     * 
     * @throws CachedFileNotFoundException
     *             si le fichier n'est pas trouvé ou pas accessible.
     */
    public static CachedStream getCachedInputStream(final File file, final int checkInterval) throws CachedFileNotFoundException, CachedFileTooLargeException {
        if (file == null) {
            throw new CachedFileNotFoundException("Fichier null n'existe pas.");
        }
        return INSTANCE.getCachedStream(file, checkInterval * 1000l);
    }

}
