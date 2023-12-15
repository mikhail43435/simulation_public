package ru.hmp.simulation.map;

import ru.hmp.simulation.model.Creature;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.EntityTypes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SimulationMap {
    void clear();

    boolean addEntity(Entity entity, Position position);

    boolean addEntityToRandomPosition(Entity entity);

    boolean addEntityRightNextPosition(Entity entity, Position positionBy);

    void addEntitiesToRandomPosition(List<Entity> entityList);

    Position getEntityPosition(Entity entity);

    Optional<Entity> getEntityAtPosition(Position position);

    List<Entity> getClosestEntities(Position position, EntityTypes entityTypes);

    List<Entity> getNClosestEntities(Position position, EntityTypes entityTypes, int number);

    List<Entity> getListOfEntities();

    List<Creature> getListOfEntities(EntityTypes entityType);

    List<Creature> getMobileEntities();

    List<Entity> getRenewableEntities(int cycleNumber);

    boolean updateEntityPosition(Entity entity, Position position);

    boolean removeEntity(Entity entity);

    boolean isEntityOnMap(Entity entity);

    boolean isValidPosition(Position position);

    boolean isPositionEmpty(Position position);

    boolean isMapFull();

    Map<String, String> getStatistic();

    Map<String, Integer> getEntitiesCount();

    int getCycleNumber();

    void startNewCycle();

    int getXMapSize();

    int getYMapSize();

    int[][] getGrid();

    Map<Position, Entity> getChangeTrackerMap();
}
