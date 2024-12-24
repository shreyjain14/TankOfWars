package me.shreyjain.model;

import me.shreyjain.config.GameConfig;

public class Tank {
    private final int initialHealth;
    private int health;
    private Position position;
    private Direction direction;
    private final String name;

    public Tank(String name) {
        this.initialHealth = GameConfig.getInitialTankHealth();
        this.health = initialHealth;
        this.direction = Direction.NORTH; // Default direction
        this.name = name;
    }

    public void reset() {
        this.health = initialHealth;
        this.position = null; // or initial position if needed
        this.direction = Direction.NORTH; // or initial direction if needed
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