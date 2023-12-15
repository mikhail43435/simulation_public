package ru.hmp.simulation.model.entities;

import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Creature;
import ru.hmp.simulation.model.EntityTypes;
import ru.hmp.simulation.model.Mobile;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;

public final class Predator extends Creature implements Mobile {

    public Predator(SimulationMap simulationMap, PathSearchAlgo pathSearchAlgo, int reproductionLimit) {
        super(simulationMap, pathSearchAlgo, reproductionLimit);
    }

    @Override
    public int makeMove() {

        if (resourceConsumptionCounter == resourceConsumptionLimit) {
            createNewEntityNearBy(EntityTypes.PREDATOR);
            resourceConsumptionCounter = 0;
            return 0;
        }

        if (targetEntity == null || !simulationMap.isEntityOnMap(this.targetEntity)) {
            if (!setTargetInRange(EntityTypes.HERBIVORE, 3)) {
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
            resourceConsumptionCounter++;
        }

        simulationMap.updateEntityPosition(this, nextPos);
        return 1;
    }
}