package com.fgodard.exceptions;

import java.io.Serializable;
import java.util.*;

import static com.fgodard.text.StringHelper.formatMessage;
import java.io.PrintWriter;

/**
 * Created by crios on 21/12/22.
 */
public class IntlException extends Exception {

    private ArrayList<Throwable> causes = new ArrayList<>();
    private Serializable[] msgParams = null;
    private String formattedMsg = null;
    private String msgKey = null;
    private static Locale locale = Locale.FRENCH;
        
    public IntlException() {
        super();
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
        filterStackTrace(this);
        formattedMsg = buildMessage(new StringBuilder()).toString();
        return formattedMsg;
    }
    
    @Override
    public String getLocalizedMessage() {
        StringBuilder sb = new StringBuilder();
        buildHeadMsg(sb, locale);
        return sb.toString();
    }
    
    public String getLocalizedMessage(Locale loc) {
        StringBuilder sb = new StringBuilder();
        buildHeadMsg(sb, loc);
        return sb.toString();
    }

    private StringBuilder buildMessage(StringBuilder sb) {
        buildHeadMsg(sb, locale);
        addStackInfo(sb,this,1);
        causes.stream().filter(e -> e != this).forEach(e -> {
            buildMessage(sb, e, 1);
        });
        return sb;
    }

    private void buildHeadMsg(StringBuilder sb, Locale loc) {
        if (msgKey == null || msgKey.isEmpty()) {
            sb.append(this.getClass().getName());
            return;
        }

        String msg;
        try {
            final String sBundleName = this.getClass().getPackage().getName().replace('.','_');
            ResourceBundle exceptionsMsg = ResourceBundle.getBundle(sBundleName,loc);
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
        String msg = exception.getMessage();
        if(msg == null || msg.trim().isEmpty()) {
            sb.append(exception.getClass().getCanonicalName());
        } else {
            sb.append(msg);
        }
        addStackInfo(sb,exception,level+1);
        Throwable cause = exception.getCause();
        if (cause != null && cause != exception) {
            buildMessage(sb, cause, level+1);
        }
        return sb;
    }

    public static void setLocale(Locale value) {
        locale = value;
    }

    private void filterStackTrace(Throwable t) {
        StackTraceElement[] elements = t.getStackTrace();
        List<StackTraceElement> newList = new ArrayList<>();
        for (StackTraceElement e : elements) {
            newList.add(e);
            if (!e.getClassName().startsWith("com.fgodard")) {
                break;
            }
        }
        t.setStackTrace(newList.toArray(new StackTraceElement[0]));        
    }
    
    
    @Override
    public void printStackTrace(PrintWriter writer) {
        writer.append(this.getMessage());
    }

    private void addStackInfo(StringBuilder sb, Throwable t, int level) {
        StackTraceElement[] elements = t.getStackTrace();
        if (elements.length > 0) {
            sb.append("\n");
            for (int i=1; i<level; i++) {
                sb.append("\t");
            }
            sb.append("@");
            sb.append(elements[0].getClassName());
            sb.append(".");
            sb.append(elements[0].getMethodName());
            sb.append(":");
            sb.append(elements[0].getLineNumber());
        }
    }

}
