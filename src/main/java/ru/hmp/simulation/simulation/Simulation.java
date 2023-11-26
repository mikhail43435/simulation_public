package ru.hmp.simulation.simulation;

public interface Simulation {
    void runSimulation(int numOfCycles) throws InterruptedException;

    void resetSimulation();
}
