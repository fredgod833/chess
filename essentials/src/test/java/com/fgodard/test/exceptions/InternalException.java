package com.fgodard.test.exceptions;

import com.fgodard.exceptions.IntlException;
import java.io.Serializable;

/**
 *
 * @author crios
 */
public class InternalException extends IntlException {
    
    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Serializable... params) {
        super(message, params);
    }
    
}
