package fr.fgodard.files.cache;

import java.io.ByteArrayInputStream;

/**
 * Flux de fichier en cache.
 */
public class CachedStream extends ByteArrayInputStream {

    private long lastCheckTs = 0;

    private long lastModifiedTime = 0;

    private boolean dirty = false;

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public long getLastCheckTs() {
        return lastCheckTs;
    }

    public void setLastCheckTs(long lastCheckTs) {
        this.lastCheckTs = lastCheckTs;
    }

    public boolean isDirty() {
        return dirty;
    }

    protected CachedStream refresh() {
        this.pos = 0;
        return this;
    }

    CachedStream(byte[] streamContent, long lastModifiedTime) {
        super(streamContent);
        lastCheckTs = System.currentTimeMillis();
        this.lastModifiedTime = lastModifiedTime;
    }

    /**
     * Retourne le contenu du cache Attention, cette méthode brise volontairement le principe d'encapsulation afin de
     * pouvoir modifier le contenu d'un fichier à la volée (typical : remplacmeent de paramètres )
     * 
     * @return le tableau représentant le fichier en cache.
     */
    public byte[] getByteArray() {
        return this.buf;
    }

    /**
     * Modifie le contenu du cache Attention, cette méthode brise volontairement le principe d'encapsulation afin de
     * pouvoir modifier le contenu d'un fichier à la volée (typical : remplacmeent de paramètres )
     */
    public void setByteArray(byte[] buffer) {
        this.buf = buffer;
        this.pos = 0;
        this.count = buf.length;
        dirty = true;
    }

}
