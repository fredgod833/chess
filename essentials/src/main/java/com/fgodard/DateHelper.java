package fr.fgodard;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by crios on 06/08/23.
 */
public class DateHelper {

    private static ThreadLocal<Map<String, SimpleDateFormat>> formatters = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<>();
        }
    };

    private DateHelper() {

    }

    private static SimpleDateFormat getFormatter(final String format) {
        SimpleDateFormat result = formatters.get().get(format);
        if (result == null) {
            result = new SimpleDateFormat(format);
            formatters.get().put(format, result);
        }
        return result;
    }


    public static String format(Date value, final String format) {
        return value == null ? null : getFormatter(format).format(value);
    }

    public static String format(Calendar value, final String format) {
        return value == null ? null : getFormatter(format).format(value);
    }

}
