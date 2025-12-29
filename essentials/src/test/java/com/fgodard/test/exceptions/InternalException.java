package com.fgodard.test.exceptions;

import com.fgodard.exceptions.IntlException;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author crios
 */
public class InternalException extends IntlException {
    
    public InternalException() {
        super();
    }

    public InternalException(String message, Serializable... params) {
        super(message, params);
    }
    
    public InternalException(Throwable cause, String message, Serializable... params) {
        super(cause, message, params);
    }
    
    public <T extends Throwable> InternalException(Collection<T> causes, String message, Serializable... params) {
        super(causes, message, params);
    }
    
}
