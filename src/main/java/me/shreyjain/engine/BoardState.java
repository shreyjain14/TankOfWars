package me.shreyjain.engine;

import me.shreyjain.config.GameConfig;

public class BoardState {
    private final Cell[][] grid;
    private final Position viewerPosition; // Position of the tank viewing this board state
    private final boolean isAdminView;

    public BoardState(Cell[][] grid, Position viewerPosition) {
        this(grid, viewerPosition, false);
    }

    public BoardState(Cell[][] grid, Position viewerPosition, boolean isAdminView) {
        this.grid = grid;
        this.viewerPosition = viewerPosition;
        this.isAdminView = isAdminView;
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && 
               position.getX() < grid.length && 
               position.getY() >= 0 && 
               position.getY() < grid[0].length;
    }

    public boolean isPositionVisible(Position position) {
        if (isAdminView || !GameConfig.isFogOfWarEnabled()) {
            return true;
        }
        
        int viewRadius = GameConfig.getFogOfWarViewRadius();
        int dx = Math.abs(position.getX() - viewerPosition.getX());
        int dy = Math.abs(position.getY() - viewerPosition.getY());
        return dx <= viewRadius && dy <= viewRadius;
    }

    public boolean hasObstacle(Position position) {
        if (!isValidPosition(position) || (!isAdminView && !isPositionVisible(position))) {
            return false;
        }
        return grid[position.getX()][position.getY()].hasObstacle();
    }

    public Tank getTankAt(Position position) {
        if (!isValidPosition(position) || (!isAdminView && !isPositionVisible(position))) {
            return null;
        }
        return grid[position.getX()][position.getY()].getTank();
    }

    public Cell[][] getGrid() {
        if (isAdminView) {
            return grid;
        }
        
        // Return only visible portions of the grid
        Cell[][] visibleGrid = new Cell[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Position pos = new Position(i, j);
                if (isPositionVisible(pos)) {
                    visibleGrid[i][j] = grid[i][j];
                } else {
                    visibleGrid[i][j] = new Cell(); // Empty cell for fog of war
                }
            }
        }
        return visibleGrid;
    }
} 