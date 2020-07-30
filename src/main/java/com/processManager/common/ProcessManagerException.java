package com.processManager.common;

public class ProcessManagerException extends Exception {

    public ProcessManagerException() {
        super();
    }

    public ProcessManagerException(String message) {
        super(message);
    }

    public ProcessManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessManagerException(Throwable cause) {
        super(cause);
    }

    protected ProcessManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
