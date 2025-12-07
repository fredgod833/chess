package com.fgodard.logs;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Created by crios on 05/08/23.
 */
class LogContext <S extends Serializable & Supplier<String>> implements Serializable {
    private String loggerName;
    private S logContext;

    String getLoggerName() {
        return loggerName;
    }

    void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    S getLogContext() {
        return logContext;
    }

    void setLogContext(S logContext) {
        this.logContext = logContext;
    }
}
