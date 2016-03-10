package com.hahazql.tools.auto.config;

/**
 * Created by zql on 2015/10/9.
 */
public class AutoConfigException extends Exception {
    public AutoConfigException() {
    }

    ;

    public AutoConfigException(String message) {
        super(message);
    }

    public AutoConfigException(Throwable throwable) {
        super(throwable);
    }

    public AutoConfigException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AutoConfigException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
