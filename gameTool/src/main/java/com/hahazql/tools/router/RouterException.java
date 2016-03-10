package com.hahazql.tools.router;

import com.hahazql.tools.exception.BaseException;

/**
 * 路由错误
 *
 * @author zql
 */
public class RouterException extends BaseException {
    /**
     *
     */
    private static final long serialVersionUID = 7948254295625111746L;

    public RouterException() {
        super();
    }

    public RouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterException(String message) {
        super(message);
    }

    public RouterException(Throwable cause) {
        super("Router出错", cause);
    }
}
