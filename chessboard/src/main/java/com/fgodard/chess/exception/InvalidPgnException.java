package com.fgodard.chess.exception;

import com.fgodard.exceptions.IntlException;

import java.io.Serializable;
import java.util.Collection;

/**
 * Exception levée lorsqu'un fichier PGN est invalide.
 *
 * <p>Le format PGN (Portable Game Notation) est le standard pour l'enregistrement
 * des parties d'échecs. Cette exception est lancée lorsque :
 * <ul>
 *   <li>Le fichier PGN est mal formaté</li>
 *   <li>Les en-têtes sont incorrects</li>
 *   <li>Les coups ne sont pas valides</li>
 * </ul>
 *
 * @author crios
 */
public class InvalidPgnException extends IntlException {

    /**
     * Construit une exception avec un message.
     *
     * @param message le message d'erreur
     */
    public InvalidPgnException(String message) {
        super(message);
    }

    /**
     * Construit une exception avec une cause et un message.
     *
     * @param cause la cause de l'exception
     * @param message le message d'erreur
     * @param <T> le type de la cause
     */
    public <T extends Throwable> InvalidPgnException(T cause, String message) {
        super(cause, message);
    }

    /**
     * Construit une exception avec plusieurs causes et un message.
     *
     * @param causes les causes de l'exception
     * @param message le message d'erreur
     * @param <T> le type des causes
     */
    public <T extends Throwable> InvalidPgnException(Collection<T> causes, String message) {
        super(causes, message);
    }

    /**
     * Construit une exception avec un message formaté.
     *
     * @param message le patron du message
     * @param params les paramètres du message
     */
    public InvalidPgnException(String message, Serializable... params) {
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
    public <T extends Throwable> InvalidPgnException(T cause, String message, Serializable... params) {
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
    public <T extends Throwable> InvalidPgnException(Collection<T> causes, String message, Serializable... params) {
        super(causes, message, params);
    }

}
