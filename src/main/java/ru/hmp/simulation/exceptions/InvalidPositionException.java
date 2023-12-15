package ru.hmp.simulation.exceptions;

public final class InvalidPositionException extends ApplicationException {

    public InvalidPositionException(String message) {
        super(message);
    }

    public InvalidPositionException(String message, Throwable cause) {
        super(message, cause);
    }
}