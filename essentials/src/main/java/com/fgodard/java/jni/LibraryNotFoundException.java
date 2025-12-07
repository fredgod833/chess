package fr.fgodard.java.jni;

import fr.fgodard.exceptions.IntlException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by crios on 21/12/22.
 */
public class LibraryNotFoundException extends IntlException {

    public LibraryNotFoundException(final String message, Serializable... params) {
        super(message, params);
    }

    public LibraryNotFoundException(Throwable cause, String message, Serializable... params) {
        super(cause, message, params);
    }

    public LibraryNotFoundException(Collection<Throwable> causes, String message, Serializable... params) {
        super(causes, message, params);
    }

}
