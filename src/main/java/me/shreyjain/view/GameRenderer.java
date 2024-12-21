package me.shreyjain.view;

import me.shreyjain.config.GameConfig;
import me.shreyjain.model.*;

import java.util.List;

public class GameRenderer {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String GRAY = "\u001B[90m";

    // Array of available colors for tanks
    private static final String[] TANK_COLORS = {RED, BLUE, MAGENTA, CYAN};
    
    private static String getTankColor(String tankName) {
        // Use hash of tank name to consistently assign same color
        return TANK_COLORS[Math.abs(tankName.hashCode()) % TANK_COLORS.length];
    }

    public static void renderAllViews(Board board, List<Player> players) {
        if (!GameConfig.isViewEnabled()) {
            return;
        }

        // Clear screen
        if (GameConfig.shouldClearScreen()) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

        // Show only the admin view
        System.out.println("\nGame Board:");
        BoardState adminView = new BoardState(board.getGrid(), null, true);
        render(adminView);

        // Show player stats
        System.out.println("\nPlayer Stats:");
        for (Player player : players) {
            Tank tank = player.getTank();
            String color = GameConfig.isColorEnabled() ? getTankColor(tank.getName()) : "";
            String reset = GameConfig.isColorEnabled() ? RESET : "";
            System.out.printf("%s%s: %d HP, Position: (%d,%d), Facing: %s%s\n", 
                color, 
                tank.getName(), 
                tank.getHealth(), 
                tank.getPosition().getX(),
                tank.getPosition().getY(),
                tank.getDirection(),
                reset);
        }
    }

    private static void render(BoardState boardState) {
        Cell[][] grid = boardState.getGrid();

        // Print column numbers
        System.out.print("   ");
        for (int i = 0; i < grid.length; i++) {
            System.out.printf(" %d ", i);
        }
        System.out.println();

        // Print top border
        printBorder(grid.length);

        // Print grid with row numbers
        for (int i = 0; i < grid.length; i++) {
            System.out.printf("%2d |", i);
            for (int j = 0; j < grid[i].length; j++) {
                renderCell(grid[i][j], i, j);
            }
            System.out.println();
        }

        // Print bottom border
        printBorder(grid.length);
        System.out.println();
    }

    private static void printBorder(int length) {
        System.out.print("   ");
        for (int i = 0; i < length; i++) {
            System.out.print("---");
        }
        System.out.println();
    }

    private static void renderCell(Cell cell, int row, int col) {
        String content;
        String color = RESET;

        if (GameConfig.isColorEnabled()) {
            if (cell == null) {
                color = GRAY;
                content = "[?]";
            } else if (cell.getTank() != null) {
                Tank tank = cell.getTank();
                color = getTankColor(tank.getName());
                content = "[" + getTankDirectionSymbol(tank) + "]";
            } else if (cell.hasObstacle()) {
                color = YELLOW;
                content = "[X]";
            } else {
                color = GREEN;
                content = "[ ]";
            }
            System.out.print(color + content + RESET);
        } else {
            if (cell == null) {
                content = "[?]";
            } else if (cell.getTank() != null) {
                content = "[" + getTankDirectionSymbol(cell.getTank()) + "]";
            } else if (cell.hasObstacle()) {
                content = "[X]";
            } else {
                content = "[ ]";
            }
            System.out.print(content);
        }
    }

    private static String getTankDirectionSymbol(Tank tank) {
        switch (tank.getDirection()) {
            case NORTH: return "↑";
            case SOUTH: return "↓";
            case EAST: return "→";
            case WEST: return "←";
            default: return "T";
        }
    }

    public static void printGameStatus(String message) {
        if (!GameConfig.isViewEnabled()) {
            return;
        }
        
        if (GameConfig.isColorEnabled()) {
            System.out.println(BLUE + "=== " + message + " ===" + RESET);
        } else {
            System.out.println("=== " + message + " ===");
        }
    }
} 