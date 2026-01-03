package com.fgodard;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by crios on 06/08/23.
 */
public class DateHelper {

    private static final Map<String, SimpleDateFormat> FORMATERS = new HashMap<>();
    private static final Lock LOCK = new ReentrantLock();

    private DateHelper() {

    }

    private static SimpleDateFormat getFormater(final String format) {
        SimpleDateFormat result = FORMATERS.get(format);
        if (result == null) {
            result = new SimpleDateFormat(format);
            FORMATERS.put(format, result);
        }
        return result;
    }

    public static String format(Date value, final String format) {
        if (format == null || value == null) {
            return null;
        }
        try {
            LOCK.lock();
            return getFormater(format).format(value);
        } finally {
            LOCK.unlock();
        }
    }

    public static String format(Calendar value, final String format) {
        if (format == null || value == null) {
            return null;
        }
        try {
            LOCK.lock();
            return getFormater(format).format(value);
        } finally {
            LOCK.unlock();
        }
    }

}
