package com.emailchecker.exception;

public class DNSLookupException extends Exception {
    public DNSLookupException(String message) {
        super(message);
    }

    public DNSLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}

