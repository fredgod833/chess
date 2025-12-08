package com.fgodard.chess.exception;

import com.fgodard.exceptions.IntlException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by crios on 23/04/23.
 */
public class InvalidPieceColorException extends IntlException {


    public InvalidPieceColorException(String message) {
        super(message);
    }

    public <T extends Throwable> InvalidPieceColorException(T cause, String message) {
        super(cause, message);
    }

    public <T extends Throwable> InvalidPieceColorException(Collection<T> causes, String message) {
        super(causes, message);
    }

    public InvalidPieceColorException(String message, Serializable... params) {
        super(message, params);
    }

    public <T extends Throwable> InvalidPieceColorException(T cause, String message, Serializable... params) {
        super(cause, message, params);
    }

    public <T extends Throwable> InvalidPieceColorException(Collection<T> causes, String message, Serializable... params) {
        super(causes, message, params);
    }
}
