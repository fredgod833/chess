package fr.fgodard.files.cache.exceptions;


import fr.fgodard.exceptions.IntlException;

public class CachedFileNotFoundException extends IntlException {

    public CachedFileNotFoundException(final String message) {
        super(message);
    }

    public CachedFileNotFoundException(final Throwable e, final String message) {
        super(e, message);
    }

    public CachedFileNotFoundException(final Throwable e, final String message, final Object... values) {
        super(e, message, values);
    }

    public CachedFileNotFoundException(final String message, final Object... values) {
        super(message, values);
    }

}
