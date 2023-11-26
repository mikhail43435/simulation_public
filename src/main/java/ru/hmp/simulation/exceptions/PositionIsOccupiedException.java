package ru.hmp.simulation.exceptions;

import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;

public class PositionIsOccupiedException extends ApplicationException {

    public PositionIsOccupiedException(String message) {
        super(message);
    }

    public PositionIsOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PositionIsOccupiedException(SimulationMap simulationMap, Position position) {
        super(String.format("Try to set entity to occupied position.%n"
                + "Map: %s%n"
                + "Position", simulationMap, position));
    }
}