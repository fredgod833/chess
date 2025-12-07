package com.fgodard.config.exceptions;

/**
 * Exception technique.
 *
 * @author fgodard
 *
 */
public class ConfigurationNotFoundException extends ConfigurationFileException {

    /**
     * Serialisation.
     */
    private static final long serialVersionUID = 6901549486947315465L;

    /**
     * Constructeur.
     *
     * @param message
     *            : le message de l'exception.
     */
    public ConfigurationNotFoundException(final String message) {
        super(message);
    }

    /**
     * Constructeur.
     *
     * @param e
     *            : l'exception d'origine
     * @param message
     *            : le message de l'exception
     */
    public ConfigurationNotFoundException(final Exception e, final String message) {
        super(e, message);
    }

    /**
     * Constructeur.
     *
     * @param e
     *            : l'exception d'origine
     * @param message
     *            : le message de l'exception avec les parametres à tracer %s, %1$s , %2$s...
     * @param values
     *            : les valeurs des paramètres à tracer.
     */
    public ConfigurationNotFoundException(final Exception e, final String message, final Object... values) {
        super(e, message, values);
    }

    /**
     * Constructeur.
     *
     * @param message
     *            : le message de l'exception.
     * @param values
     *            : les paramètres à tracer.
     */
    public ConfigurationNotFoundException(final String message, final Object... values) {
        super(message, values);
    }

}
