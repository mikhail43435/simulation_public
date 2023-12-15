package ru.hmp.simulation.exceptions;

import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Entity;

public final class NoFreeSpaceOnMapException extends ApplicationException {

    public NoFreeSpaceOnMapException(String message) {
        super(message);
    }

    public NoFreeSpaceOnMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFreeSpaceOnMapException(SimulationMap simulationMap, Entity entity) {
        super(String.format("There is not free points on the simulation map to add new entity.%n"
                + "Map: %s%n"
                + "Entity: %s", simulationMap, entity));
    }
}