package com.fgodard.files.cache.exceptions;

import com.fgodard.exceptions.IntlException;

public class CachedFileTooLargeException extends IntlException {

    public CachedFileTooLargeException(final String message) {
        super(message);
    }

    public CachedFileTooLargeException(final Throwable e, final String message) {
        super(e, message);
    }

    public CachedFileTooLargeException(final Throwable e, final String message, final Object... values) {
        super(e, message, values);
    }

    public CachedFileTooLargeException(final String message, final Object... values) {
        super(message, values);
    }

}
