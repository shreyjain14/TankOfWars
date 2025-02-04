package me.shreyjain.engine;

public enum Direction {
    NORTH, EAST, SOUTH, WEST;

    public Direction rotateRight() {
        return values()[(ordinal() + 1) % 4];
    }

    public Direction rotateLeft() {
        return values()[(ordinal() + 3) % 4];
    }

    public Direction opposite() {
        return values()[(ordinal() + 2) % 4];
    }
} 