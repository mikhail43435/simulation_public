package ru.hmp.simulation.map;

import ru.hmp.simulation.exceptions.InvalidPositionException;
import ru.hmp.simulation.exceptions.NoFreeSpaceOnMapException;
import ru.hmp.simulation.exceptions.NoSuchEntityOnMapFoundException;
import ru.hmp.simulation.exceptions.PositionIsOccupiedException;
import ru.hmp.simulation.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class TwoHashMapsSimulationMap implements SimulationMap {

    private static final Random RANDOM = new Random();
    private final Map<Entity, Position> mapEntityPos = new HashMap<>();
    private final Map<Position, Entity> mapPosEntity = new HashMap<>();
    private final HashMap<Integer, List<Entity>> mapRenewableEntities = new HashMap<>();
    private final HashMap<Position, Entity> changeTracker = new HashMap<>();
    private final int[][] delta = new int[][]{{1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};
    private final int xMapSize;
    private final int yMapSize;
    private final int mapDimension;
    private final int maxRECycleLength;
    private int cycleCounter = 0;

    public TwoHashMapsSimulationMap(int xMapSize, int yMapSize, int maxRECycleLength) {
        this.xMapSize = xMapSize;
        this.yMapSize = yMapSize;
        this.mapDimension = xMapSize * yMapSize;
        this.maxRECycleLength = maxRECycleLength;
    }

    @Override
    public void clear() {
        mapPosEntity.clear();
        mapEntityPos.clear();
        mapRenewableEntities.clear();
        changeTracker.clear();
        cycleCounter = 0;
    }

    @Override
    public boolean addEntity(Entity entity, Position position) {
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
        mapEntityPos.put(entity, position);
        mapPosEntity.put(position, entity);
        changeTracker.put(position, entity);
        return true;
    }

    @Override
    public boolean addEntityToRandomPosition(Entity entity) {
        if (isMapFull()) {
            throw new NoFreeSpaceOnMapException(this, entity);
        }
        Position position;
        do {
            position = PositionFactory.of(RANDOM.nextInt(getXMapSize()), RANDOM.nextInt(getYMapSize()));
        } while (!isPositionEmpty(position));
        return addEntity(entity, position);
    }

    @Override
    public boolean addEntityRightNextPosition(Entity entity, Position positionBy) {
        List<Position> listOfPossiblePositions = new ArrayList<>();

        for (int[] del : delta) {
            Position tempPosition = PositionFactory.of(positionBy.getX() + del[0], positionBy.getY() + del[1]);
            if (isValidPosition(tempPosition) && isPositionEmpty(tempPosition)) {
                listOfPossiblePositions.add(tempPosition);
            }
        }

        if (listOfPossiblePositions.isEmpty()) {
            return false;
        }

        return addEntity(entity,
                listOfPossiblePositions.get(RANDOM.nextInt(listOfPossiblePositions.size())));
    }

    @Override
    public void addEntitiesToRandomPosition(List<Entity> entityList) {
        entityList.forEach(this::addEntityToRandomPosition);
    }

    @Override
    public Position getEntityPosition(Entity entity) {
        if (!isEntityOnMap(entity)) {
            throw new NoSuchEntityOnMapFoundException(this, entity);
        }
        return mapEntityPos.get(entity);
    }

    @Override
    public Optional<Entity> getEntityAtPosition(Position position) {
        return Optional.ofNullable(mapPosEntity.get(position));
    }

    /**
     * Find closest object (objects if more than one) of given type to the point using Chebyshev distance concept
     *
     * @param position    - point around which the search is carried out
     * @param entityTypes - type of entity to search for
     * @return
     */
    @Override
    public List<Entity> getClosestEntities(Position position, EntityTypes entityTypes) {

        List<Entity> list = new ArrayList<>(mapEntityPos.keySet());

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

    /**
     * Find closest n objects of given type to the point using Chebyshev distance concept
     *
     * @param position    - point around which the search is carried out
     * @param entityTypes - type of entity to search for
     * @return
     */
    @Override
    public List<Entity> getNClosestEntities(Position position, EntityTypes entityTypes, int number) {

        if (number < 0) {
            throw
                    new IllegalArgumentException("Invalid argument (number) value "
                            + "in TwoHashMapsSimulationMap.getNClosestEntities.");
        }

        Queue<PositionDistancePair> queue = new PriorityQueue<>(Comparator.reverseOrder());

        List<Entity> listOfEntities = new ArrayList<>(mapEntityPos.keySet());

        listOfEntities.
                stream().
                filter(entity -> entity.getClass() == entityTypes.getClassType()).
                forEach(entity -> {
                    int distance = Math.max(Math.abs(position.getX() - getEntityPosition(entity).getX()),
                            Math.abs(position.getY() - getEntityPosition(entity).getY()));

                    if (queue.size() > number) {
                        if (distance < queue.peek().distance) {
                            queue.poll();
                            queue.add(new PositionDistancePair(entity, distance));
                        }
                    } else {
                        queue.add(new PositionDistancePair(entity, distance));
                    }
                });
        return queue.stream().
                map(positionDistancePair -> positionDistancePair.entity).
                collect(Collectors.toList());
    }

    @Override
    public List<Entity> getListOfEntities() {
        return new ArrayList<>(mapEntityPos.keySet());
    }

    @Override
    public List<Creature> getListOfEntities(EntityTypes entityType) {
        return mapEntityPos.keySet().
                stream().
                filter(e -> e.getClass() == entityType.getClassType()).
                map(Creature.class::cast).
                collect(Collectors.toList());
    }

    @Override
    public List<Creature> getMobileEntities() {
        return mapEntityPos.keySet().
                stream().
                filter(e -> e instanceof Mobile).
                map(Creature.class::cast).
                collect(Collectors.toList());
    }

    @Override
    public List<Entity> getRenewableEntities(int cycleNumber) {
        return new ArrayList<>(mapRenewableEntities.getOrDefault(cycleNumber, Collections.emptyList()));
    }

    @Override
    public boolean updateEntityPosition(Entity entity, Position newPosition) {
        if (!isEntityOnMap(entity)) {
            throw new NoSuchEntityOnMapFoundException(this, entity);
        }
        if (!isPositionEmpty(newPosition)) {
            throw new PositionIsOccupiedException(this, newPosition);
        }
        Position prevPosition = mapEntityPos.get(entity);
        mapEntityPos.put(entity, newPosition);
        mapPosEntity.remove(prevPosition);
        mapPosEntity.put(newPosition, entity);
        changeTracker.put(prevPosition, null);
        changeTracker.put(newPosition, entity);
        return true;
    }

    @Override
    public boolean removeEntity(Entity entity) {
        Position positionOfRemovingEntity = mapEntityPos.remove(entity);
        if (positionOfRemovingEntity == null) {
            return false;
        }
        mapPosEntity.remove(positionOfRemovingEntity);
        changeTracker.put(positionOfRemovingEntity, null);
        if (entity instanceof RenewableEntity) {
            addEntityToREMap(entity);
        }
        return true;
    }

    @Override
    public boolean isEntityOnMap(Entity entity) {
        return mapEntityPos.containsKey(entity);
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
        return !mapPosEntity.containsKey(position);
    }

    @Override
    public boolean isMapFull() {
        return mapEntityPos.size() == mapDimension;
    }

    @Override
    public Map<String, String> getStatistic() {
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("Number of cycle", String.valueOf(this.cycleCounter));
        map.put("Map width, points", String.valueOf(this.xMapSize));
        map.put("Map height, points", String.valueOf(this.yMapSize));
        map.put("Number of points", String.valueOf(this.mapDimension));
        map.put("Number of points occupied", String.valueOf(this.mapPosEntity.size()));
        map.put("Number of points occupied, %", String.valueOf(this.mapPosEntity.size() * 100 / this.mapDimension));
        map.put("Number of points free", String.valueOf(this.mapDimension - this.mapEntityPos.size()));
        map.put("Number of points free, %",
                String.valueOf((this.mapDimension - this.mapEntityPos.size()) * 100 / this.mapDimension));
        map.put("Renewable entities collected",
                String.valueOf(this.mapRenewableEntities.getOrDefault(cycleCounter, Collections.emptyList()).size()));
        map.put("Total number of entities", String.valueOf(this.mapEntityPos.size()));
        getEntitiesCount().
                forEach((key, value) ->
                        map.put(String.format("Number of '%s' entities", key), String.valueOf(value)));

        return Collections.unmodifiableMap(map);
    }

    @Override
    public Map<String, Integer> getEntitiesCount() {
        HashMap<String, Integer> entityCountMap = new HashMap<>();
        this.mapEntityPos.forEach((key, value) ->
                entityCountMap.merge(key.getClass().getSimpleName(), 1, Integer::sum));
        return Collections.unmodifiableMap(entityCountMap);
    }

    @Override
    public int getCycleNumber() {
        return cycleCounter;
    }

    @Override
    public void startNewCycle() {
        changeTracker.clear();
        mapRenewableEntities.remove(getCycleNumber() - maxRECycleLength);
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

        for (Position position : new ArrayList<>(mapPosEntity.keySet())) {
            grid[position.getY()][position.getX()] = 1;
        }
        return grid;
    }

    @Override
    public Map<Position, Entity> getChangeTrackerMap() {
        return new HashMap<>(changeTracker);
    }

    private void addEntityToREMap(Entity entity) {
        mapRenewableEntities.computeIfAbsent(cycleCounter, e -> new ArrayList<>());
        mapRenewableEntities.get(cycleCounter).add(entity);
    }

    private static class PositionDistancePair implements Comparable<PositionDistancePair> {
        private final Entity entity;
        private final int distance;

        public PositionDistancePair(Entity entity, int distance) {
            this.entity = entity;
            this.distance = distance;
        }

        @Override
        public int compareTo(PositionDistancePair o) {
            return this.distance - o.distance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PositionDistancePair)) {
                return false;
            }
            PositionDistancePair that = (PositionDistancePair) o;
            return distance == that.distance;
        }

        @Override
        public int hashCode() {
            return Objects.hash(distance);
        }
    }
}