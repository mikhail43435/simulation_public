package ru.hmp.simulation.exceptions;

public class IdleSimulationException extends ApplicationException {

    public IdleSimulationException(String message) {
        super(message);
    }

    public IdleSimulationException(String message, Throwable cause) {
        super(message, cause);
    }
}