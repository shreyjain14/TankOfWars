package me.shreyjain.model;

import java.util.Objects;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position move(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Position(x, y - 1);
            case SOUTH:
                return new Position(x, y + 1);
            case EAST:
                return new Position(x + 1, y);
            case WEST:
                return new Position(x - 1, y);
            default:
                throw new IllegalStateException("Unknown direction: " + direction);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
} 