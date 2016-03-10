package com.hahazql.tools.exception;/**
 * Created by zql on 15/7/28.
 */

/**
 * Created by zql on 15/7/28.
 *
 * @className BaseException
 * @classUse
 */
public class BaseException extends Exception {
    private static final long serialVersionUID = -1103164294134360436L;

    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
