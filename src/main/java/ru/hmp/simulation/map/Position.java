package ru.hmp.simulation.map;

import java.util.Objects;
import java.util.WeakHashMap;

public class Position {

    private static final WeakHashMap<Integer, Position> POOL = new WeakHashMap<>();
    private final int x;
    private final int y;

    private Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y) {
        int key = x * 10_000 + y;
        Position result = POOL.get(key);
        if (result == null) {
            result = new Position(x, y);
            POOL.put(key, result);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;

        return getX() == position.getX() && getY() == position.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}