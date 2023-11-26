package ru.hmp.simulation.map;

import ru.hmp.simulation.model.Creature;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.EntityTypes;

import java.util.List;
import java.util.Optional;

public interface SimulationMap {
    void clearMap();

    boolean addEntityToMapToPosition(Entity entity, Position position);

    boolean addEntityToMapToRandomPosition(Entity entity);

    boolean addEntityRightNextPosition(Entity entity, Position positionBy);

    void addListOfEntitiesToMapToRandomPosition(List<Entity> entityList);

    Position getEntityPosition(Entity entity);

    Optional<Entity> getEntityAtPosition(Position position);

    List<Entity> getClosesEntitiesOfGivenType(Position position, EntityTypes entityTypes);

    List<Entity> getListOfEntityLeft();

    List<Creature> getListOfEntityCertainTypeLeft(EntityTypes entityType);

    List<Entity> getRenewableEntityForGivenCycle(int cycleNumber);

    boolean updateEntityPosition(Entity entity, Position position);

    boolean removeEntity(Entity entity);

    boolean isEntityOnMap(Entity entity);

    boolean isValidPosition(Position position);

    boolean isPositionEmpty(Position position);

    boolean isMapFull();

    int getCycleNumber();

    void incrementCycleCounter();

    int getXMapSize();

    int getYMapSize();

    int[][] getGrid();
}
