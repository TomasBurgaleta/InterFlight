package com.ryanair.api.biz;

public class InterconnectionException extends Exception {


    public InterconnectionException(String message) {
        super(message);
    }

    public InterconnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
