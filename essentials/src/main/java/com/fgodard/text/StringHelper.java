package com.fgodard.text;

import java.io.Serializable;
import java.util.Arrays;
import java.util.IllegalFormatException;

/**
 * Created by crios on 06/08/23.
 */
public class StringHelper {

    private static final String EMPTY_STRING = "";

    private StringHelper() {

    }

    public static String nvl(final String value, final String replacement) {
        return value==null ? replacement : value;
    }

    /**
     * Trim une chaine, retourne une chaine vide si la chaine est nulle
     * @param value : chaine
     * @return chaine trimée non vide ou null.
     */
    public static String trim(final String value) {
        if (value==null) {
            return EMPTY_STRING;
        }
        return value.trim();
    }

    /**
     * Contruit un message contenant des paramètre quelconques
     * @param format 
     * @param params
     * @return
     */
    public static String formatMessage(String format, Serializable... params) {
        if (format == null) {
            return null;
        }
        if (params == null) {
            return format;
        }

        for (int i = 0; i < params.length; i++) {
            params[i] = BeanHelper.beanToString(params[i]);
        }

        String result;
        try {
            result = String.format(format, params);

        } catch (IllegalFormatException e) {
            result = format +", params=" + Arrays.toString(params);

        }

        return result;
    }

}
