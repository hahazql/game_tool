package com.hahazql.tools.msg;

/**
 * 消息解析过程发生的异常
 */
public class MessageParseException extends Exception {
    private static final long serialVersionUID = 1L;

    public MessageParseException() {
        super();
    }

    public MessageParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageParseException(String message) {
        super(message);
    }

    public MessageParseException(Throwable cause) {
        super(cause);
    }

}
