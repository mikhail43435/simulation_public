package ru.hmp.simulation.exceptions;

public final class LoadResourcesException extends ApplicationException {

    public LoadResourcesException(String message) {
        super(message);
    }

    public LoadResourcesException(String message, Throwable cause) {
        super(message, cause);
    }
}