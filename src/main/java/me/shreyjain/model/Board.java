package me.shreyjain.model;

import me.shreyjain.config.GameConfig;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class Board {
    private final Cell[][] grid;
    private final int size;

    public Board(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void generateObstacles() {
        int totalObstacles = (int) (size * size * GameConfig.getObstacleDensity());
        Random random = new Random();
        
        // Keep track of corner positions to avoid placing obstacles there
        Set<Position> cornerPositions = new HashSet<>();
        cornerPositions.add(new Position(0, 0));
        cornerPositions.add(new Position(0, size - 1));
        cornerPositions.add(new Position(size - 1, 0));
        cornerPositions.add(new Position(size - 1, size - 1));

        int placedObstacles = 0;
        while (placedObstacles < totalObstacles) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            Position pos = new Position(x, y);

            // Don't place obstacles in corners where tanks start
            if (!cornerPositions.contains(pos) && !grid[x][y].hasObstacle()) {
                grid[x][y].setObstacle(true);
                placedObstacles++;
            }
        }
    }

    public void placeTank(Tank tank, Position position) {
        if (isValidPosition(position)) {
            // Remove tank from current position if it exists
            if (tank.getPosition() != null) {
                Cell currentCell = grid[tank.getPosition().getX()][tank.getPosition().getY()];
                currentCell.setTank(null);
            }
            grid[position.getX()][position.getY()].setTank(tank);
            tank.setPosition(position);
        }
    }

    public BoardState getBoardState(Position viewerPosition) {
        return new BoardState(grid, viewerPosition);
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && 
               position.getX() < size && 
               position.getY() >= 0 && 
               position.getY() < size;
    }

    public boolean hasObstacle(Position position) {
        if (!isValidPosition(position)) {
            return false;
        }
        return grid[position.getX()][position.getY()].hasObstacle();
    }

    public Tank getTankAt(Position position) {
        if (!isValidPosition(position)) {
            return null;
        }
        return grid[position.getX()][position.getY()].getTank();
    }

    // Add this method to get the raw grid for admin view
    public Cell[][] getGrid() {
        return grid;
    }


    public String getFormattedObstaclePositions() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (hasObstacle(new Position(x, y))) {
                    sb.append(String.format("(%d,%d) ", x, y));
                }
            }
        }
        return sb.toString().trim();
    }
} 