package me.shreyjain.engine;

public class Cell {
    private Tank tank;
    private boolean hasObstacle;

    public Cell() {
        this.tank = null;
        this.hasObstacle = false;
    }

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public boolean hasObstacle() {
        return hasObstacle;
    }

    public void setObstacle(boolean hasObstacle) {
        this.hasObstacle = hasObstacle;
    }
} 