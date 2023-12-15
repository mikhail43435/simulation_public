package ru.hmp.simulation.map;

import java.util.Objects;

public final class PositionImpl implements Position {

    private final int x;
    private final int y;

    public PositionImpl(int x, int y) {
        this.x = x;
        this.y = y;
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
        return "Position{"
                + "x=" + x
                + ", y=" + y
                + '}';
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Position o) {
        if (this.y == o.getY()) {
            return this.x - o.getX();
        } else {
            return this.y - o.getY();
        }
    }
}