package com.fgodard.exceptions;

import java.io.Serializable;
import java.util.*;

import static com.fgodard.StringHelper.formatMessage;

/**
 * Created by crios on 21/12/22.
 */
public class IntlException extends Exception {

    private ArrayList<Throwable> causes = new ArrayList<>();
    private Serializable[] msgParams = null;
    private String formattedMsg = null;
    private String msgKey = null;
    private static Locale locale = Locale.FRENCH;

    public IntlException(final String message) {
        super(message);
        this.msgKey = message == null ? null : message.trim();
    }

    public <T extends Throwable> IntlException(T cause, final String message) {
        super(message);
        this.msgKey = message == null ? null : message.trim();
        this.causes.add(cause);
    }

    public <T extends Throwable> IntlException(Collection<T> causes, final String message) {
        super(message);
        this.msgKey = message == null ? null : message.trim();
        this.causes.addAll(causes);
    }

    public IntlException(final String message, Serializable... params) {
        super(message);
        this.msgKey = message == null ? null : message.trim();
        this.msgParams = params;
    }

    public <T extends Throwable> IntlException(T cause, final String message, Serializable... params) {
        super(message);
        this.msgKey = message == null ? null : message.trim();
        this.msgParams = params;
        this.causes.add(cause);
    }

    public <T extends Throwable> IntlException(Collection<T> causes, final String message, Serializable... params) {
        super(message);
        this.msgKey = message == null ? null : message.trim();
        this.msgParams = params;
        this.causes.addAll(causes);
    }

    @Override
    public String getMessage() {
        if (formattedMsg != null) {
            return formattedMsg;
        }
        formattedMsg = buildMessage(new StringBuilder()).toString();
        return formattedMsg;
    }

    private StringBuilder buildMessage(StringBuilder sb) {
        buildHeadMsg(sb);
        causes.stream().filter(e -> e != this).forEach(e -> {
            buildMessage(sb, e, 1);
        });
        return sb;
    }

    private void buildHeadMsg(StringBuilder sb) {
        if (msgKey == null || msgKey.isEmpty()) {
            sb.append(this.getClass().getCanonicalName());
            return;
        }

        String msg;
        try {
            final String sBundleName = this.getClass().getPackage().getName().replace('.','_');
            ResourceBundle exceptionsMsg = PropertyResourceBundle.getBundle(sBundleName,locale);
            msg = exceptionsMsg.getString(msgKey).trim();

        } catch (MissingResourceException e) {
            msg = msgKey;
        }

        if (msgParams == null || msgParams.length == 0) {
            sb.append(msg);

        } else {
            try {
                sb.append(formatMessage(msg,msgParams)).toString();

            } catch (IllegalFormatException e) {
                sb.append(msg);
                if (msgParams != null || msgParams.length>0) {
                    sb.append(Arrays.toString(msgParams));
                }
            }

        }
    }

    private StringBuilder buildMessage(StringBuilder sb, Throwable exception, int level) {
        sb.append(System.lineSeparator());
        for (int i=0; i<level; i++) {
            sb.append("\t");
        }
        sb.append("cause: ");
        sb.append(exception.getMessage());
        Throwable cause = exception.getCause();
        if (cause != null && cause != exception) {
            buildMessage(sb, cause, level+1);
        }
        return sb;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale value) {
        locale = value;
    }

}
