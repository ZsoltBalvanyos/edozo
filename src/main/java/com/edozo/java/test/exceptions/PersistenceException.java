package com.edozo.java.test.exceptions;

public class PersistenceException extends RuntimeException {
    public PersistenceException(Throwable e) {
        super(e);
    }
    public PersistenceException(String msg) {
        super(msg);
    }
}
