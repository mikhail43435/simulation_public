package ru.hmp.simulation.exceptions;

import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Entity;

public class NoSuchEntityOnMapFoundException extends ApplicationException {

    public NoSuchEntityOnMapFoundException(String message) {
        super(message);
    }

    public NoSuchEntityOnMapFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEntityOnMapFoundException(SimulationMap simulationMap, Entity entity) {
        super(String.format("Entity not found on the map. Entity: %s%n"
                + " Map: %s%n", entity, simulationMap));
    }
}