package com.fgodard.chess.exception;

import com.fgodard.exceptions.IntlException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Exception levée lorsqu'une position d'échecs est invalide.
 *
 * <p>Cette exception est lancée lorsque :
 * <ul>
 *   <li>La chaîne FEN est mal formatée</li>
 *   <li>La chaîne LLP est invalide</li>
 *   <li>La position contient des erreurs (ex: pièces manquantes)</li>
 * </ul>
 *
 * @author crios
 */
public class InvalidPositionException extends IntlException {

    /**
     * Construit une exception avec un message.
     *
     * @param message le message d'erreur
     */
    public InvalidPositionException(String message) {
        super(message);
    }

    /**
     * Construit une exception avec une cause et un message.
     *
     * @param cause la cause de l'exception
     * @param message le message d'erreur
     * @param <T> le type de la cause
     */
    public <T extends Throwable> InvalidPositionException(T cause, String message) {
        super(cause, message);
    }

    /**
     * Construit une exception avec plusieurs causes et un message.
     *
     * @param causes les causes de l'exception
     * @param message le message d'erreur
     * @param <T> le type des causes
     */
    public <T extends Throwable> InvalidPositionException(Collection<T> causes, String message) {
        super(causes, message);
    }

    /**
     * Construit une exception avec un message formaté.
     *
     * @param message le patron du message
     * @param params les paramètres du message
     */
    public InvalidPositionException(String message, Serializable... params) {
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
    public <T extends Throwable> InvalidPositionException(T cause, String message, Serializable... params) {
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
    public <T extends Throwable> InvalidPositionException(Collection<T> causes, String message, Serializable... params) {
        super(causes, message, params);
    }
}
