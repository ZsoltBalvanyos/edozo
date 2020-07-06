package com.edozo.java.test.exceptions;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(Throwable e) {
        super(e);
    }

    public RecordNotFoundException(String msg) {
        super(msg);
    }
}
