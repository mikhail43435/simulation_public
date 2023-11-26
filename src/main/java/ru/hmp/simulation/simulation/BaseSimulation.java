package ru.hmp.simulation.simulation;

import ru.hmp.simulation.exceptions.IdleSimulationException;
import ru.hmp.simulation.iosim.SimMapOutput;
import ru.hmp.simulation.map.SimulationMap;
import ru.hmp.simulation.model.Creature;
import ru.hmp.simulation.model.Entity;
import ru.hmp.simulation.model.EntityTypes;

import java.util.ArrayList;
import java.util.List;

public class BaseSimulation implements Simulation {

    public static final int IDLE_THRESHOLD = 3;
    private final SimulationMap simulationMap;
    private final SimMapOutput mapOutput;
    private final List<Entity> entityList;
    private final int repetitionRate;
    private final int renewableResourcesCycleLength;

    public BaseSimulation(SimulationMap simulationMap,
                          SimMapOutput mapOutput,
                          List<Entity> entityList,
                          int repetitionRate,
                          int renewableResourcesCycleLength) {
        this.simulationMap = simulationMap;
        this.mapOutput = mapOutput;
        this.entityList = entityList;
        this.repetitionRate = repetitionRate;
        this.renewableResourcesCycleLength = renewableResourcesCycleLength;
        initSimMap();
    }

    private int nextTurn() {
        simulationMap.incrementCycleCounter();
        List<Creature> list = new ArrayList<>();
        list.addAll(simulationMap.getListOfEntityCertainTypeLeft(EntityTypes.HERBIVORE));
        list.addAll(simulationMap.getListOfEntityCertainTypeLeft(EntityTypes.PREDATOR));
        return list.stream().mapToInt(Creature::makeMove).sum();
    }

    @Override
    public void runSimulation(int numOfCycles) {
        int idleCounter = 0;
        for (int i = 0; i < numOfCycles; i++) {
            if (nextTurn() == 0) {
                idleCounter++;
                if (idleCounter == IDLE_THRESHOLD) {
                    throw new IdleSimulationException(
                            String.format("Idle situation (%d in a row) occurred at cycle # %d",
                                    IDLE_THRESHOLD,
                                    simulationMap.getCycleNumber()));
                }
            } else {
                idleCounter = 0;
            }
            mapOutput.displayMap(simulationMap);
            simulationMap.addListOfEntitiesToMapToRandomPosition(simulationMap.
                    getRenewableEntityForGivenCycle(
                            simulationMap.getCycleNumber() - renewableResourcesCycleLength));
            try {
                Thread.sleep(repetitionRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void initSimMap() {
        simulationMap.addListOfEntitiesToMapToRandomPosition(entityList);
    }

    @Override
    public void resetSimulation() {
        simulationMap.clearMap();
        prepareMap();
    }

    private void prepareMap() {
        simulationMap.addListOfEntitiesToMapToRandomPosition(entityList);
    }
}