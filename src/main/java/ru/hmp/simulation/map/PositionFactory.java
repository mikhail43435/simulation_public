package ru.hmp.simulation.map;

import java.util.WeakHashMap;

public class PositionFactory {

    private static final WeakHashMap<Integer, Position> POOL = new WeakHashMap<>();

    public static Position of(int x, int y) {
        int key = x * 10_000 + y;
        Position result = POOL.get(key);
        if (result == null) {
            result = new PositionImpl(x, y);
            POOL.put(key, result);
        }
        return result;
    }
}
