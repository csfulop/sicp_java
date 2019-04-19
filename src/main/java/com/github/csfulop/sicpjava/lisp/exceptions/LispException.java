package com.github.csfulop.sicpjava.lisp.exceptions;

public class LispException extends RuntimeException {
    public LispException(String message) {
        super(message);
    }

    public LispException(Throwable cause) {
        super(cause);
    }
}
