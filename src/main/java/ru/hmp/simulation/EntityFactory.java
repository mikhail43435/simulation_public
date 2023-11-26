package ru.hmp.simulation;

import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.*;
import ru.hmp.simulation.pathsearch.PathSearchAlgo;

import java.util.ArrayList;
import java.util.List;

public final class EntityFactory {

    private EntityFactory() {
    }

    public static Entity createEntity(EntityTypes entityType,
                                      SimulationMap simulationMap,
                                      PathSearchAlgo pathSearchAlgo,
                                      int reproductionLimit) {
        Entity entity;
        switch (entityType) {
            case PREDATOR -> entity = new Predator(simulationMap, pathSearchAlgo, 0);
            case HERBIVORE -> entity = new Herbivore(simulationMap, pathSearchAlgo, reproductionLimit);
            case ROCK -> entity = new Rock();
            case GRASS -> entity = new Grass();
            case TREE -> entity = new Tree();
            default -> throw new IllegalStateException("Invalid entity type: " + entityType);
        }
        return entity;
    }

    public static List<Entity> createNumOfEntities(EntityTypes entityType,
                                                   SimulationMap simulationMap,
                                                   PathSearchAlgo pathSearchAlgo,
                                                   int reproductionLimit,
                                                   int numOfEntities) {
        List<Entity> list = new ArrayList<>();
        for (int i = 0; i < numOfEntities; i++) {
            list.add(createEntity(entityType, simulationMap, pathSearchAlgo, reproductionLimit));
        }
        return list;
    }
}