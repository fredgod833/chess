package fr.fgodard.logs;

import fr.fgodard.BeanHelper;
import fr.fgodard.config.exceptions.ConfigurationFileException;
import fr.fgodard.config.exceptions.ConfigurationNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import static fr.fgodard.StringHelper.formatMessage;
import static fr.fgodard.config.Configuration.readConfigTextFile;
import static fr.fgodard.config.Configuration.writeConfigTextFile;

/**
 * Created by crios on 05/08/23.
 */
public class LogManager {

        private static final Level ALERT_LEVEL = Level.SEVERE;
        private static final Level STACK_TRACE_LEVEL = Level.WARNING;
        private static final String FRAMEWORK_LOGGER_NAME = "fgodard";
        private static final String STACK_FILTER = "StackFilterList";

        private static final ThreadLocal<LogContext> CONTEXT_SUPPLIER = new ThreadLocal<LogContext>() {
            @Override
            protected synchronized LogContext initialValue() {
                return new LogContext();
            }
        };

        private static final ThreadLocal<HashMap<String,Logger>> LOGGER_MAP = new ThreadLocal<HashMap<String,Logger>>() {
            @Override
            protected synchronized HashMap<String,Logger> initialValue() {
                return new HashMap<>();
            }
        };

        public <C extends Supplier<String> & Serializable> void setLogsContext(C mdc) {
            CONTEXT_SUPPLIER.get().setLogContext(mdc);
        }

        public void clearLogsContext() {
            CONTEXT_SUPPLIER.get().setLoggerName(null);
            CONTEXT_SUPPLIER.get().setLogContext(null);
            CONTEXT_SUPPLIER.remove();
        }

        /**
         * Utility Class Private Constructor.
         */
        private LogManager() {
        }

        public static String buildMessageWithStack(String message, Object... params) {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append(formatMessage(message, params));
            for (int i = 1; i < stack.length; i++) {
                StackTraceElement stackElement = stack[i];
                sb.append(stackElement.getClassName());
                sb.append(":");
                sb.append(stackElement.getLineNumber());
                sb.append("(");
                sb.append(stackElement.getMethodName());
                sb.append(")\n");
            }
            return sb.toString();
        }

        public static void debugWithCallStack(String message, Object... params) {
            String msg = buildMessageWithStack(message, params);
            debug(msg);
        }

        public static void infoWithCallStack(String message, Object... params) {
            String msg = buildMessageWithStack(message, params);
            info(msg);
        }

        public static void warnWithCallStack(String message, Object... params) {
            String msg = buildMessageWithStack(message, params);
            warn(msg);
        }

        public static void errorWithCallStack(String message, Object... params) {
            String msg = buildMessageWithStack(message, params);
            error(msg);
        }

        private static StackTraceElement getCallerContext() {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            int i = 5;
            while (i < stack.length) {
                if (!stack[i].getClassName().startsWith("fr.verspieren.log.")) {
                    return stack[i];
                }
                i++;
            }
            return stack[stack.length - 1];
        }

        private static void appendContextInfos(StringBuilder sb) {
            Supplier<String> context = (Supplier<String>) CONTEXT_SUPPLIER.get().getLogContext();
            if (context == null) {
                return;
            }
            String sContext = context.get();
            sContext = sContext == null ? null : sContext.trim();
            if (sContext == null || sContext.isEmpty()) {
                return;
            }
            sb.append(sContext);
        }

        private static void appendCallerContext(StringBuilder sb) {
            StackTraceElement callerCtx = getCallerContext();
            String className = callerCtx.getClassName();
            String[] classPackages = className.split("[\\.]");
            for (int i = 0; i < classPackages.length - 1; i++) {
                sb.append(classPackages[i].charAt(0));
            }
            sb.append(".");
            sb.append(classPackages[classPackages.length - 1]);
            sb.append(":");
            sb.append(String.valueOf(callerCtx.getLineNumber()));
            sb.append("(");
            sb.append(callerCtx.getMethodName());
            sb.append(")");
        }

        public static String buildLogMessage(String message, Throwable exception, boolean writeStack) {
            StringBuilder sb = new StringBuilder();
            appendContextInfos(sb);
            sb.append(" ");
            appendCallerContext(sb);
            sb.append(" ");
            if (message != null) {
                sb.append(message);
            }
            if (exception != null) {
                if (message != null) {
                    sb.append(System.lineSeparator());
                    sb.append("\tdetail: ");
                }
                appendExceptionDetails(sb, exception, 1, writeStack);
            }
            return sb.toString();
        }

        private static void writeLog(final String loggerName, Level level, Throwable t, Supplier<String> messageSupplier,
                                     final String message, Serializable... params) {

            String msg;
            boolean sendAlert = (level.intValue() >= ALERT_LEVEL.intValue() && level.intValue() > Level.INFO.intValue());
            boolean writeStack = (level.intValue() >= STACK_TRACE_LEVEL.intValue() && level.intValue() > Level.INFO.intValue());
            Logger logger = LOGGER_MAP.get().get(loggerName);
            if (logger == null) {
                logger = createLogger(loggerName);
                LOGGER_MAP.get().put(loggerName, logger);
            }

            if (logger != null && !logger.isLoggable(level) && !sendAlert) {
                // on logges si logger == null (dans system out)
                // ou si la priorité du logger le permet
                // ou si on doit lever une alerte.
                return;
            }

            if (messageSupplier != null) {
                msg = messageSupplier.get();
            } else {
                msg = buildMessage(message, params);
            }

            String log = buildLogMessage(msg, t, writeStack);
            if (logger==null) {
                // pas de loggeur : niveau mini = info pour le Sys.out
                if (level.intValue() > Level.INFO.intValue()) {
                    System.out.println(log);
                }
            } else {
                logger.log(level,log);
            }
            /*
            if (sendAlert) {
                AlertManagerFactory.getAlertManager().alert(t, log);
            }
            */
        }

    private static String buildMessage(String message, Serializable[] params) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        if(params == null || params.length==0){
            return message;
        }

        for (int i=0; i<params.length; i++) {
            params[i] = BeanHelper.beanToString(params[i]);
        }

        return String.format(message,params);

    }

    private static Logger createLogger(String loggerName) {
        return Logger.getLogger(loggerName);
    }

    /**
     * Filtre la pile d'appel en fonction des classes définies.
     *
     * @param ste
     *            : pile d'appel à filter
     *
     * @return la stackTrace filtrée.
     */
    public static StackTraceElement[] filterStackTrace(final StackTraceElement[] ste, final HashSet<String> filter) {
        /*
        if (ste == null || ste.length == 0) {
            return ste;
        }
        List<StackTraceElement> temp = new ArrayList<>();
        for (int idx = 0; idx < ste.length; idx++) {
            if (filter.getPrefix(ste[idx].getClassName()) == null) {
                // pas dans la liste des exclusions
                temp.add(ste[idx]);
            }
        }
        return temp.toArray(new StackTraceElement[temp.size()]);
        */
        return ste;
    }

    /**
     * Inscrit la pile d'appel dans un buffer.
     *
     * @param messageBuffer
     *            le buffer à alimenter.
     * @param ste
     *            la stackTrace à inscrire.
     */
    private static void appendStackTrace(final StringBuilder messageBuffer, final StackTraceElement[] ste,
                                         final int tabCount) {
        if (ste == null) {
            return;
        }

        if (ste.length > 1) {
            appendTab(messageBuffer, tabCount);
            messageBuffer.append("stack: ");
            messageBuffer.append(ste[1].toString());
            messageBuffer.append("\n");
        }

        for (int i = 2; i < ste.length; i++) {
            appendTab(messageBuffer, tabCount + 1);
            messageBuffer.append(ste[i].toString());
            messageBuffer.append("\n");
        }
    }

    private static HashSet<String> getStackExclusions() {
        try {
            return readConfigTextFile(STACK_FILTER, new HashSet<>());
        } catch (ConfigurationNotFoundException e) {
            e.printStackTrace();
            try {
                writeConfigTextFile(STACK_FILTER, new HashSet<>());
            } catch (ConfigurationFileException e1) {
                e1.printStackTrace();
            }
        } catch (ConfigurationFileException e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    /**
     * Construit le détail de l'exception (liste des cause + pile d'appel de la cause originale).
     *
     * @param messageBuffer
     *            buffer d'écriture de l'exception.
     * @param exception
     *            l'exception à tracer.
     */
    protected static void appendExceptionDetails(StringBuilder messageBuffer, Throwable exception, int tabCount,
                                                 boolean writeStack) {
        messageBuffer.append(exception.getClass().getName());
        messageBuffer.append(", ");
        messageBuffer.append(exception.getMessage());

        StackTraceElement[] ste = exception.getStackTrace();
        HashSet<String> stackFilter = getStackExclusions();
        if (ste != null && stackFilter != null) {
            ste = filterStackTrace(ste,stackFilter);
        }
        if (ste != null && ste.length > 0) {
            messageBuffer.append(" at " + exception.getStackTrace()[0]);
        }

        messageBuffer.append(System.lineSeparator());
        if (hasCause(exception)) {
            appendTab(messageBuffer, tabCount + 1);
            messageBuffer.append("cause: ");
            appendExceptionDetails(messageBuffer, exception.getCause(), tabCount + 1, writeStack);

        } else {
            if (writeStack) {
                appendStackTrace(messageBuffer, ste, tabCount + 1);
            }

        }
    }

    private static void appendTab(final StringBuilder messageBuffer, final int tabCount) {
        for (int i = 0; i < tabCount; i++) {
            messageBuffer.append("\t");
        }
    }

    /**
     * indique si une cause existe.
     *
     * @param t
     *            : l'exception
     *
     * @return true si la cause de l'exception existe.
     */
    private static boolean hasCause(final Throwable t) {
        Throwable cause = t.getCause();
        return (cause != null && cause != t);
    }

    public static boolean hasCause(Throwable e, Class<?> causeClass) {
        if (e == null) {
            return false;
        }
        if (causeClass.isAssignableFrom(e.getClass())) {
            return true;
        }
        Throwable cause = e.getCause();
        if (cause != e) {
            return hasCause(cause, causeClass);
        }
        return false;
    }

    public static <E extends Throwable> E getTopException(Throwable e, Class<E> exceptionClass) {
        if (e == null || exceptionClass == null) {
            return null;
        }
        if (exceptionClass.isAssignableFrom(e.getClass())) {
            return (E) e;
        }
        Throwable cause = e.getCause();
        if (cause != e) {
            return getTopException(cause, exceptionClass);
        }
        return null;
    }

    public static boolean hasCauseMessage(Throwable e, CharSequence msg) {
        if (e == null) {
            return false;
        }
        if (e.getMessage() != null && e.getMessage().contains(msg)) {
            return true;
        }
        Throwable cause = e.getCause();
        if (cause != e) {
            return hasCauseMessage(cause, msg);
        }
        return false;
    }

    public static void debugFwk(Throwable t, String message, Serializable... params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.FINE, t, null, message, params);
        }

        public static void debug(Throwable t, String message, Serializable...params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.FINE, t, null, message, params);
        }

        public static void infoFwk(Throwable t, String message, Serializable...params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, t, null, message, params);
        }

        public static void info(Throwable t, String message, Serializable...params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.INFO, t, null, message, params);
        }

        public static void warnFwk(Throwable t, String message, Serializable...params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.WARNING, t, null, message, params);
        }

        public static void warn(Throwable t, String message, Serializable...params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.WARNING, t, null, message, params);
        }

        public static void errorFwk(Throwable t, String message, Serializable...params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.SEVERE, t, null, message, params);
        }

        public static void error(Throwable t, String message, Serializable...params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, t, null, message, params);
        }

        public static void debugFwk(Throwable t, String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.FINE, t, null, message, new Serializable[0]);
        }

        public static void debug(Throwable t, String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.FINE, t, null, message, new Serializable[0]);
        }

        public static void infoFwk(Throwable t, String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, t, null, message, new Serializable[0]);
        }

        public static void info(Throwable t, String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.INFO, t, null, message, new Serializable[0]);
        }

        public static void warnFwk(Throwable t, String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.WARNING, t, null, message, new Serializable[0]);
        }

        public static void warn(Throwable t, String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.WARNING, t, null, message, new Serializable[0]);
        }

        public static void errorFwk(Throwable t, String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.SEVERE, t, null, message, new Serializable[0]);
        }

        public static void error(Throwable t, String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, t, null, message, new Serializable[0]);
        }

        public static void debugFwk(Throwable t) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.FINE, t, null, null, new Serializable[0]);
        }

        public static void debug(Throwable t) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.FINE, t, null, null, new Serializable[0]);
        }

        public static void infoFwk(Throwable t) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, t, null, null, new Serializable[0]);
        }

        public static void info(Throwable t) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, t, null, null, new Serializable[0]);
        }

        public static void warnFwk(Throwable t) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.WARNING, t, null, null, new Serializable[0]);
        }

        public static void warn(Throwable t) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.WARNING, t, null, null, new Serializable[0]);
        }

        public static void errorFwk(Throwable t) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.SEVERE, t, null, null, new Serializable[0]);
        }

        public static void error(Throwable t) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, t, null, null, new Serializable[0]);
        }

        public static void debugFwk(String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.FINE, null, null, message, new Serializable[0]);
        }

        public static void debug(String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.FINE, null, null, message, new Serializable[0]);
        }

        public static void infoFwk(String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, null, null, message, new Serializable[0]);
        }

        public static void info(String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.INFO, null, null, message, new Serializable[0]);
        }

        public static void warnFwk(String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.WARNING, null, null, message, new Serializable[0]);
        }

        public static void warn(String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.WARNING, null, null, message, new Serializable[0]);
        }

        public static void errorFwk(String message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.SEVERE, null, null, message, new Serializable[0]);
        }

        public static void error(String message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, null, null, message, new Serializable[0]);
        }

        public static void debugFwk(String message, Serializable...params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.FINE, null, null, message, params);
        }

        public static void debug(String message, Serializable...params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.FINE, null, null, message, params);
        }

        public static void infoFwk(String message, Serializable...params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, null, null, message, params);
        }

        public static void info(String message, Serializable...params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.INFO, null, null, message, params);
        }

        public static void warnFwk(String message, Serializable...params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.WARNING, null, null, message, params);
        }

        public static void warn(String message, Serializable...params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.WARNING, null, null, message, params);
        }

        public static void errorFwk(String message, Serializable...params) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.SEVERE, null, null, message, params);
        }

        public static void error(String message, Serializable... params) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, null, null, message, params);
        }

        public static void debugFwk(Supplier<String> message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.FINE, null, message, null, new Serializable[0]);
        }

        public static void debug(Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.FINE, null, message, null, new Serializable[0]);
        }

        public static void infoFwk(Supplier<String> message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, null, message, null, new Serializable[0]);
        }

        public static void info(Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.INFO, null, message, null, new Serializable[0]);
        }

        public static void warnFwk(Supplier<String> message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.WARNING, null, message, null, new Serializable[0]);
        }

        public static void warn(Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.WARNING, null, message, null, new Serializable[0]);
        }

        public static void errorFwk(Supplier<String> message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.SEVERE, null, message, null, new Serializable[0]);
        }

        public static void error(Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, null, message, null, new Serializable[0]);
        }

        public static void debugFwk(Throwable t, Supplier<String> message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.FINE, t, message, null, new Serializable[0]);
        }

        public static void debug(Throwable t, Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.FINE, t, message, null, new Serializable[0]);
        }

        public static void infoFwk(Throwable t, Supplier<String> message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.INFO, t, message, null, new Serializable[0]);
        }

        public static void info(Throwable t, Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.INFO, t, message, null, new Serializable[0]);
        }

        public static void warnFwk(Throwable t, Supplier<String> message) {
            writeLog(FRAMEWORK_LOGGER_NAME, Level.WARNING, t, message, null, new Serializable[0]);
        }

        public static void warn(Throwable t, Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.WARNING, t, message, null, new Serializable[0]);
        }

        public static void errorFwk(Throwable t, Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, t, message, null, new Serializable[0]);
        }

        public static void error(Throwable t, Supplier<String> message) {
            writeLog(CONTEXT_SUPPLIER.get().getLoggerName(), Level.SEVERE, t, message, null, new Serializable[0]);
        }

}
