package ru.hmp.simulation.model;

import ru.hmp.simulation.map.Position;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;

import java.util.*;

public abstract class Creature extends Entity {

    protected static final Random RANDOM = new Random();
    protected final SimulationMap simulationMap;
    protected PathSearchAlgo pathSearchAlgo;
    protected Entity targetEntity;
    protected Deque<Position> pathToTarget;
    protected int reproductionLimit;

    protected Creature(SimulationMap simulationMap, PathSearchAlgo pathSearchAlgo, int reproductionLimit) {
        this.simulationMap = simulationMap;
        this.pathSearchAlgo = pathSearchAlgo;
        this.reproductionLimit = reproductionLimit;
    }

    protected boolean setPathToTarget(Entity targetEntity) {
        Optional<List<Position>> path = pathSearchAlgo.findPath(simulationMap.getGrid(),
                simulationMap.getEntityPosition(this),
                simulationMap.getEntityPosition(targetEntity));

        if (path.isEmpty()) {
            return false;
        }
        this.pathToTarget = new ArrayDeque<>(path.get());
        this.pathToTarget.removeFirst();
        return true;
    }

    /**
     * Setting random entity form closest entities as a target
     *
     * @param entityType - type of entity to search
     * @return true - target has been set, false - there is no eligible targets on the map
     */
    protected boolean setTargetOfGiveType(EntityTypes entityType) {
        List<Entity> closesEntities =
                simulationMap.getClosesEntitiesOfGivenType(simulationMap.getEntityPosition(this), entityType);

        if (closesEntities.isEmpty()) {
            return false;
        }

        this.targetEntity = closesEntities.get(RANDOM.nextInt(closesEntities.size()));
        return true;
    }

    /**
     * Remove first and last points (entity position and grass position itself)
     * from path and put left point to Map as Position objects
     *
     * @param list list positions
     */

    public abstract int makeMove();

    @Override
    public String toString() {
        String result = System.lineSeparator() + "Creature{" +
                "Class=" + this.getClass().getSimpleName() + System.lineSeparator() +
                " SimMap=" + simulationMap + System.lineSeparator() +
                " pathSearchAlgo=" + pathSearchAlgo + System.lineSeparator() +
                " targetEntity=" + targetEntity + System.lineSeparator() +
                " pathToTarget=" + pathToTarget + System.lineSeparator() +
                " reproductionLimit=" + reproductionLimit;

        if (simulationMap != null && simulationMap.isEntityOnMap(this)) {
            result += System.lineSeparator() + " position on the map=" + simulationMap.getEntityPosition(this).toString();
        }

        result += '}';
        return result;
    }
}