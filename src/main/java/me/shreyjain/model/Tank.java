package me.shreyjain.model;

public class Tank {
    private int health;
    private Position position;
    private Direction direction;
    private final String name;

    public Tank(int initialHealth, String name) {
        this.health = initialHealth;
        this.direction = Direction.NORTH; // Default direction
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void damage(int amount) {
        this.health = Math.max(0, this.health - amount);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public String getName() {
        return name;
    }
} 