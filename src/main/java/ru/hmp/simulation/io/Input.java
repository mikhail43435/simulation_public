package ru.hmp.simulation.io;

public interface Input {
    String askStr(String question);

    int askInt(String question);
}