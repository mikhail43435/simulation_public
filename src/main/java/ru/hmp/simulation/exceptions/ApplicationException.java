package ru.hmp.simulation.exceptions;

public abstract class ApplicationException extends RuntimeException {

    protected ApplicationException() {
    }

    protected ApplicationException(String message) {
        super(message);
    }

    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}