package ru.hmp.simulation.util;

import java.util.concurrent.TimeUnit;

public class TimeMeter {

    private static long startTime;

    private TimeMeter() {
    }

    public static void start() {
        startTime = System.nanoTime();
    }

    public static void stop() {
        System.out.println(TimeUnit.MILLISECONDS.
                convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
        startTime = 0;
    }
}