package ru.hmp.simulation.io;

public final class ConsoleOutput implements Output {

    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public void printLineSeparator() {
        System.out.println();
    }
}