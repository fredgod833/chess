package com.fgodard.chess.exception;

import com.fgodard.exceptions.IntlException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Exception levée lorsqu'une case de l'échiquier est invalide.
 *
 * <p>Cette exception est lancée lorsque :
 * <ul>
 *   <li>La notation algébrique est incorrecte (ex: "z9")</li>
 *   <li>Les coordonnées sont hors de l'échiquier</li>
 *   <li>La case spécifiée n'existe pas</li>
 * </ul>
 *
 * @author crios
 */
public class InvalidCellException extends IntlException {

    /**
     * Construit une exception avec un message.
     *
     * @param message le message d'erreur
     */
    public InvalidCellException(String message) {
        super(message);
    }

    /**
     * Construit une exception avec une cause et un message.
     *
     * @param cause la cause de l'exception
     * @param message le message d'erreur
     * @param <T> le type de la cause
     */
    public <T extends Throwable> InvalidCellException(T cause, String message) {
        super(cause, message);
    }

    /**
     * Construit une exception avec plusieurs causes et un message.
     *
     * @param causes les causes de l'exception
     * @param message le message d'erreur
     * @param <T> le type des causes
     */
    public <T extends Throwable> InvalidCellException(Collection<T> causes, String message) {
        super(causes, message);
    }

    /**
     * Construit une exception avec un message formaté.
     *
     * @param message le patron du message
     * @param params les paramètres du message
     */
    public InvalidCellException(String message, Serializable... params) {
        super(message, params);
    }

    /**
     * Construit une exception avec une cause et un message formaté.
     *
     * @param cause la cause de l'exception
     * @param message le patron du message
     * @param params les paramètres du message
     * @param <T> le type de la cause
     */
    public <T extends Throwable> InvalidCellException(T cause, String message, Serializable... params) {
        super(cause, message, params);
    }

    /**
     * Construit une exception avec plusieurs causes et un message formaté.
     *
     * @param causes les causes de l'exception
     * @param message le patron du message
     * @param params les paramètres du message
     * @param <T> le type des causes
     */
    public <T extends Throwable> InvalidCellException(Collection<T> causes, String message, Serializable... params) {
        super(causes, message, params);
    }

}
