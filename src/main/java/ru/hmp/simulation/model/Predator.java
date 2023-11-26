package ru.hmp.simulation.model;

import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;

public class Predator extends Creature {

    public Predator(SimulationMap simulationMap, PathSearchAlgo pathSearchAlgo, int reproductionLimit) {
        super(simulationMap, pathSearchAlgo, reproductionLimit);
    }

    @Override
    public int makeMove() {

        if (targetEntity == null || !simulationMap.isEntityOnMap(this.targetEntity)) {
            if (!setTargetOfGiveType(EntityTypes.HERBIVORE)) {
                return 0;
            }
        }

        if (!setPathToTarget(targetEntity)) {
            return 0;
        }

        Position nextPos = pathToTarget.removeFirst();

        if (pathToTarget.isEmpty()) {
            simulationMap.removeEntity(targetEntity);
            targetEntity = null;
        }

        simulationMap.updateEntityPosition(this, nextPos);
        return 1;
    }
}