package com.hahazql.tools.event;/**
 * Created by zql on 15/12/23.
 */

/**
 * Created by zql on 15/12/23.
 *
 * @className EventListenerParseException
 * @classUse
 */
public class EventListenerParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EventListenerParseException() {
        super();
    }

    public EventListenerParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventListenerParseException(String message) {
        super(message);
    }

    public EventListenerParseException(Throwable cause) {
        super(cause);
    }

}
