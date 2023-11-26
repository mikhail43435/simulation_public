package ru.hmp.simulation.map;

import ru.hmp.simulation.exceptions.InvalidPositionException;
import ru.hmp.simulation.exceptions.NoFreeSpaceOnMapException;
import ru.hmp.simulation.exceptions.NoSuchEntityOnMapFoundException;
import ru.hmp.simulation.exceptions.PositionIsOccupiedException;
import ru.hmp.simulation.model.Creature;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.EntityTypes;
import ru.hmp.simulation.model.RenewableEntity;

import java.util.*;
import java.util.stream.Collectors;

public class TwoHashMapsSimulationMap implements SimulationMap {

    private static final Random RANDOM = new Random();
    private final Map<Entity, Position> mapEntityPosition = new HashMap<>();
    private final Map<Position, Entity> mapPositionEntity = new HashMap<>();
    private final int[][] delta = new int[][]{{1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};
    private final int xMapSize;
    private final int yMapSize;
    private final int numOfAllPoints;
    private final HashMap<Integer, List<Entity>> renewableEntityMap = new HashMap<>();
    private int cycleCounter = 0;

    public TwoHashMapsSimulationMap(int xMapSize, int yMapSize) {
        this.xMapSize = xMapSize;
        this.yMapSize = yMapSize;
        this.numOfAllPoints = xMapSize * yMapSize;
    }

    @Override
    public void clearMap() {
        mapPositionEntity.clear();
        mapEntityPosition.clear();
        renewableEntityMap.clear();
        cycleCounter = 0;
    }

    @Override
    public boolean addEntityToMapToPosition(Entity entity, Position position) {
        if (isEntityOnMap(entity)) {
            return false;
        }
        if (!isValidPosition(position)) {
            throw new InvalidPositionException(String.format("Invalid position of entity:%n"
                            + "entity: %s%n"
                            + "position: %s%n"
                            + "map: %s",
                    entity.toString(), position.toString(), this.toString()));
        }
        if (isMapFull()) {
            throw new NoFreeSpaceOnMapException(this, entity);
        }
        mapEntityPosition.put(entity, position);
        mapPositionEntity.put(position, entity);
        return true;
    }

    @Override
    public boolean addEntityToMapToRandomPosition(Entity entity) {
        if (isMapFull()) {
            throw new NoFreeSpaceOnMapException(this, entity);
        }
        Position position;
        do {
            position = Position.of(RANDOM.nextInt(getXMapSize()), RANDOM.nextInt(getYMapSize()));
        } while (!isPositionEmpty(position));
        return addEntityToMapToPosition(entity, position);
    }

    @Override
    public boolean addEntityRightNextPosition(Entity entity, Position positionBy) {
        List<Position> listOfPossiblePositions = new ArrayList<>();

        for (int[] del : delta) {
            Position tempPosition = Position.of(positionBy.getX() + del[0], positionBy.getY() + del[1]);
            if (isValidPosition(tempPosition) && isPositionEmpty(tempPosition)) {
                listOfPossiblePositions.add(tempPosition);
            }
        }

        if (listOfPossiblePositions.isEmpty()) {
            return false;
        }

        return addEntityToMapToPosition(entity,
                listOfPossiblePositions.get(RANDOM.nextInt(listOfPossiblePositions.size())));
    }

    @Override
    public void addListOfEntitiesToMapToRandomPosition(List<Entity> entityList) {
        for (Entity entity : entityList) {
            addEntityToMapToRandomPosition(entity);
        }
    }

    @Override
    public Position getEntityPosition(Entity entity) {
        if (!isEntityOnMap(entity)) {
            throw new NoSuchEntityOnMapFoundException(this, entity);
        }
        return mapEntityPosition.get(entity);
    }

    @Override
    public Optional<Entity> getEntityAtPosition(Position position) {
        return Optional.ofNullable(mapPositionEntity.get(position));
    }

    /**
     * Find closest object (objects if more than one) of given type to the point using Chebyshev distance concept
     * @param position - point around which the search is carried out
     * @param entityTypes - type of entity to search for
     * @return
     */
    @Override
    public List<Entity> getClosesEntitiesOfGivenType(Position position, EntityTypes entityTypes) {

        List<Entity> list = new ArrayList<>(mapEntityPosition.keySet());

        int[] minDistance = new int[]{Integer.MAX_VALUE};

        list.
                stream().
                filter(entity -> entity.getClass() == entityTypes.getClassType()).
                forEach(entity -> {
                    int distance = Math.max(Math.abs(position.getX() - getEntityPosition(entity).getX()),
                            Math.abs(position.getY() - getEntityPosition(entity).getY()));
                    minDistance[0] = Math.min(minDistance[0], distance);
                });

        return list.
                stream().
                filter(entity -> entity.getClass() == entityTypes.getClassType()).
                filter(entity ->
                        Math.max(Math.abs(position.getX() - getEntityPosition(entity).getX()),
                                Math.abs(position.getY() - getEntityPosition(entity).getY()))
                                == minDistance[0]).
                collect(Collectors.toList());
    }

    @Override
    public List<Entity> getListOfEntityLeft() {
        return new ArrayList<>(mapEntityPosition.keySet());
    }

    @Override
    public List<Creature> getListOfEntityCertainTypeLeft(EntityTypes entityType) {
        return mapEntityPosition.keySet().
                stream().
                filter(e -> e.getClass() == entityType.getClassType()).
                map(Creature.class::cast).
                collect(Collectors.toList());
    }

    @Override
    public List<Entity> getRenewableEntityForGivenCycle(int cycleNumber) {
        return renewableEntityMap.getOrDefault(cycleNumber, Collections.emptyList());
    }

    @Override
    public boolean updateEntityPosition(Entity entity, Position position) {
        if (!isEntityOnMap(entity)) {
            throw new NoSuchEntityOnMapFoundException(this, entity);
        }
        if (!isPositionEmpty(position)) {
            throw new PositionIsOccupiedException(this, position);
        }
        Position prevPosition = mapEntityPosition.get(entity);
        mapEntityPosition.put(entity, position);
        mapPositionEntity.remove(prevPosition);
        mapPositionEntity.put(position, entity);
        return true;
    }

    @Override
    public boolean removeEntity(Entity entity) {
        Position positionOfRemovingEntity = mapEntityPosition.remove(entity);
        if (positionOfRemovingEntity == null) {
            return false;
        }

        mapPositionEntity.remove(positionOfRemovingEntity);

        if (entity instanceof RenewableEntity) {
            addEntityToRenewableEntityMap(entity);
        }
        return true;
    }

    @Override
    public boolean isEntityOnMap(Entity entity) {
        return mapEntityPosition.containsKey(entity);
    }

    @Override
    public boolean isValidPosition(Position position) {
        return position.getX() >= 0
                && position.getY() >= 0
                && position.getX() < getXMapSize()
                && position.getY() < getYMapSize();
    }

    @Override
    public boolean isPositionEmpty(Position position) {
        return !mapPositionEntity.containsKey(position);
    }

    @Override
    public boolean isMapFull() {
        return mapEntityPosition.size() == numOfAllPoints;
    }

    @Override
    public int getCycleNumber() {
        return cycleCounter;
    }

    @Override
    public void incrementCycleCounter() {
        cycleCounter++;
    }

    @Override
    public int getXMapSize() {
        return xMapSize;
    }

    @Override
    public int getYMapSize() {
        return yMapSize;
    }

    /**
     * Generating matrix for path-find algo
     */
    @Override
    public int[][] getGrid() {
        int[][] grid = new int[getYMapSize()][getXMapSize()];

        for (Position position : new ArrayList<>(mapPositionEntity.keySet())) {
            grid[position.getY()][position.getX()] = 1;
        }
        return grid;
    }

    private void addEntityToRenewableEntityMap(Entity entity) {
        renewableEntityMap.computeIfAbsent(cycleCounter, e -> new ArrayList<>());
        renewableEntityMap.get(cycleCounter).add(entity);
    }
}