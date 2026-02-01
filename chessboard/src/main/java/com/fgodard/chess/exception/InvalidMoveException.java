package com.fgodard.chess.exception;

import com.fgodard.exceptions.IntlException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Exception levée lorsqu'un coup d'échecs est invalide.
 *
 * <p>Cette exception est lancée dans les cas suivants :
 * <ul>
 *   <li>La pièce ne peut pas atteindre la case de destination</li>
 *   <li>Le coup mettrait le roi en échec</li>
 *   <li>Le roque n'est pas autorisé</li>
 *   <li>Le format du coup est invalide</li>
 * </ul>
 *
 * @author crios
 */
public class InvalidMoveException extends IntlException {

    /**
     * Construit une exception avec un message.
     *
     * @param message le message d'erreur
     */
    public InvalidMoveException(String message) {
        super(message);
    }

    /**
     * Construit une exception avec une cause et un message.
     *
     * @param cause la cause de l'exception
     * @param message le message d'erreur
     * @param <T> le type de la cause
     */
    public <T extends Throwable> InvalidMoveException(T cause, String message) {
        super(cause, message);
    }

    /**
     * Construit une exception avec plusieurs causes et un message.
     *
     * @param causes les causes de l'exception
     * @param message le message d'erreur
     * @param <T> le type des causes
     */
    public <T extends Throwable> InvalidMoveException(Collection<T> causes, String message) {
        super(causes, message);
    }

    /**
     * Construit une exception avec un message formaté.
     *
     * @param message le patron du message
     * @param params les paramètres du message
     */
    public InvalidMoveException(String message, Serializable... params) {
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
    public <T extends Throwable> InvalidMoveException(T cause, String message, Serializable... params) {
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
    public <T extends Throwable> InvalidMoveException(Collection<T> causes, String message, Serializable... params) {
        super(causes, message, params);
    }

}
