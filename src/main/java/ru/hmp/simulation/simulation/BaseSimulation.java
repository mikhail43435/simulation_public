package ru.hmp.simulation.simulation;

import ru.hmp.simulation.exceptions.IdleSimulationException;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Creature;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.EntityTypes;
import ru.hmp.simulation.render.SimMapRenderer;

import java.util.ArrayList;
import java.util.List;

public final class BaseSimulation implements Simulation {

    public static final int IDLE_THRESHOLD = 3;
    private final SimulationMap simulationMap;
    private final List<Entity> entityList;
    private final int repetitionRate;
    private final int renewableResourcesCycleLength;
    private volatile boolean isRunning = true;
    private SimMapRenderer mapRenderer;

    public BaseSimulation(SimulationMap simulationMap,
                          List<Entity> entityList,
                          int repetitionRate,
                          int renewableResourcesCycleLength) {
        this.simulationMap = simulationMap;
        this.entityList = entityList;
        this.repetitionRate = repetitionRate;
        this.renewableResourcesCycleLength = renewableResourcesCycleLength;
        fillMap();
    }

    public void setSimulationMapRenderer(SimMapRenderer simulationMapOutput) {
        this.mapRenderer = simulationMapOutput;
        render();
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            run(Integer.MAX_VALUE);
        }
    }

    @Override
    public void run(int numOfCycles) {
        isRunning = true;
        int idleCounter = 0;
        for (int i = 0; i < numOfCycles; i++) {
            if (!isRunning) {
                break;
            }
            if (nextTurn() == 0) {
                idleCounter++;
                checkIdleThreshold(idleCounter);
            } else {
                idleCounter = 0;
            }
            render();
            try {
                Thread.sleep(repetitionRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public int nextTurn() {
        simulationMap.startNewCycle();
        simulationMap.addEntitiesToRandomPosition(
                simulationMap.getRenewableEntities(simulationMap.getCycleNumber()
                        - renewableResourcesCycleLength));
        List<Creature> list = new ArrayList<>();
        list.addAll(simulationMap.getListOfEntities(EntityTypes.HERBIVORE));
        list.addAll(simulationMap.getListOfEntities(EntityTypes.PREDATOR));
        return list.stream().mapToInt(Creature::makeMove).sum();
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void reset() {
        simulationMap.clear();
        fillMap();
    }

    @Override
    public void render() {
        mapRenderer.renderMap();
    }

    private void fillMap() {
        simulationMap.addEntitiesToRandomPosition(entityList);
    }

    private void checkIdleThreshold(int idleCounter) {
        if (idleCounter == IDLE_THRESHOLD) {
            throw new IdleSimulationException(
                    String.format("Idle state (%d in a row) occurred at simulation cycle # %d",
                            IDLE_THRESHOLD,
                            simulationMap.getCycleNumber()));
        }
    }
}