package ru.hmp.simulation.simulation;

public interface Simulation {
    int nextTurn();

    void run();

    void run(int numOfCycles);

    void stop();

    void reset();

    void render();
}
