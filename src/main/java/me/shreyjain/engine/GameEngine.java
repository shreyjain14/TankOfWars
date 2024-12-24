package me.shreyjain.engine;

import me.shreyjain.config.GameConfig;
import me.shreyjain.model.*;
import me.shreyjain.util.GameLogger;
import me.shreyjain.view.GameRenderer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GameEngine {
    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex;
    private boolean gameOver;
    private final GameLogger logger;
    
    // For stalemate detection
    private final LinkedList<GameState> previousStates;
    private static final int STALEMATE_DETECTION_LENGTH = 40; // Increase to allow longer detection

    private int turnCount = 0;
    private final int maxTurns;

    public GameEngine(List<Player> players, String logFilePath) {
        if (players.size() < GameConfig.getMinPlayers() || players.size() > GameConfig.getMaxPlayers()) {
            throw new IllegalArgumentException("Invalid number of players");
        }
        this.board = new Board(GameConfig.getBoardSize());
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.previousStates = new LinkedList<>();
        this.logger = new GameLogger(logFilePath);
        this.maxTurns = GameConfig.getMaxTurns();
        
        initializeGame();
    }

    private void initializeGame() {
        board.generateObstacles();
        placeTanks();
        
        // Log initial game state
        logger.log("Game initialized with " + players.size() + " players");
        logger.log("Board size: " + GameConfig.getBoardSize());
        logger.log("Obstacle density: " + GameConfig.getObstacleDensity());
        logger.log("Max turns: " + GameConfig.getMaxTurns());

        for (Player player : players) {
            Tank tank = player.getTank();
            logger.log(String.format("Player %s starting at position (%d,%d) facing %s", 
                player.getName(), 
                tank.getPosition().getX(), 
                tank.getPosition().getY(),
                tank.getDirection()));
        }

        // Log obstacle positions
        String obstacles = board.getFormattedObstaclePositions();
        logger.log("Obstacles at positions: " + obstacles);
    }

    private void placeTanks() {
        int boardSize = GameConfig.getBoardSize();
        List<Position> startPositions = new ArrayList<>();

        if (players.size() == 2) {
            // For 2 players, place them at (0, 0) and (N, N)
            startPositions.add(new Position(0, 0));
            startPositions.add(new Position(boardSize - 1, boardSize - 1));
        } else {
            // For more players, use the corners
            startPositions.add(new Position(0, 0));
            startPositions.add(new Position(0, boardSize - 1));
            startPositions.add(new Position(boardSize - 1, 0));
            startPositions.add(new Position(boardSize - 1, boardSize - 1));
        }

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Position startPos = startPositions.get(i);
            board.placeTank(player.getTank(), startPos);
        }
    }

    public void executeNextTurn() {
        if (gameOver) return;

        // Check if any tanks are still active
        long activeTanks = players.stream().filter(p -> !p.getTank().isDestroyed()).count();
        if (activeTanks <= 1) {
            gameOver = true;
            logger.log("Game Over! No tanks remaining.");
            return;
        }

        Player currentPlayer = players.get(currentPlayerIndex);
        logger.log("Starting turn for " + currentPlayer.getName());
        
        // Render all views
        GameRenderer.printGameStatus(currentPlayer.getName() + "'s turn");
        GameRenderer.renderAllViews(board, players);
        
        // Record current state for stalemate detection
        GameState currentState = new GameState(players);
        previousStates.add(currentState);
        if (previousStates.size() > STALEMATE_DETECTION_LENGTH) {
            previousStates.removeFirst();
        }
        
        // Check for stalemate
        if (isStalemate()) {
            gameOver = true;
            String message = "Game Over! Stalemate detected!";
            logger.log(message);
            GameRenderer.printGameStatus(message);
            return;
        }

        List<Move> moves = currentPlayer.getNextMoves(
            board.getBoardState(currentPlayer.getTank().getPosition())
        );

        // Limit the number of moves to execute to movesPerTurn
        int movesPerTurn = GameConfig.getMovesPerTurn();
        for (int i = 0; i < Math.min(moves.size(), movesPerTurn); i++) {
            Move move = moves.get(i);
            executeMove(currentPlayer, move);
            String moveMessage = currentPlayer.getName() + " executed " + move;
            logger.log(moveMessage);
            GameRenderer.printGameStatus(moveMessage);
            
            if (gameOver) {
                String winMessage = "Game Over! " + currentPlayer.getName() + " wins!";
                logger.log(winMessage);
                GameRenderer.printGameStatus(winMessage);
                logger.close();
                return;
            }
            
            if (GameConfig.isViewEnabled() && GameConfig.getMoveDelay() > 0) {
                try {
                    Thread.sleep(GameConfig.getMoveDelay());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (currentPlayerIndex == 0) {
            turnCount++;
            if (turnCount >= maxTurns) {
                System.out.println("Game declared a draw due to turn limit.");
                gameOver = true;
            }
        }
    }

    private void executeMove(Player player, Move move) {
        Tank tank = player.getTank();
        Position currentPos = tank.getPosition();
        Direction currentDir = tank.getDirection();
        
        switch (move) {
            case MOVE_FORWARD:
                Position forwardPos = currentPos.move(currentDir);
                if (isValidMove(forwardPos)) {
                    board.placeTank(tank, forwardPos);
                    logger.log(String.format("%s moved forward to (%d,%d)", 
                        player.getName(), forwardPos.getX(), forwardPos.getY()));
                } else {
                    logger.log(String.format("%s attempted invalid forward move to (%d,%d)", 
                        player.getName(), forwardPos.getX(), forwardPos.getY()));
                }
                break;
            case MOVE_BACKWARD:
                Position backwardPos = currentPos.move(currentDir.opposite());
                if (isValidMove(backwardPos)) {
                    board.placeTank(tank, backwardPos);
                    logger.log(String.format("%s moved backward to (%d,%d)", 
                        player.getName(), backwardPos.getX(), backwardPos.getY()));
                } else {
                    logger.log(String.format("%s attempted invalid backward move to (%d,%d)", 
                        player.getName(), backwardPos.getX(), backwardPos.getY()));
                }
                break;
            case ROTATE_LEFT:
                tank.setDirection(currentDir.rotateLeft());
                logger.log(String.format("%s rotated left to face %s", 
                    player.getName(), tank.getDirection()));
                break;
            case ROTATE_RIGHT:
                tank.setDirection(currentDir.rotateRight());
                logger.log(String.format("%s rotated right to face %s", 
                    player.getName(), tank.getDirection()));
                break;
            case SHOOT:
                logger.log(String.format("%s shooting in direction %s", 
                    player.getName(), tank.getDirection()));
                handleShoot(player);
                break;
        }
    }

    private boolean isValidMove(Position newPos) {
        return board.isValidPosition(newPos) && 
               !board.hasObstacle(newPos) && 
               board.getTankAt(newPos) == null;
    }

    private void handleShoot(Player shooter) {
        Tank shooterTank = shooter.getTank();
        Position currentPos = shooterTank.getPosition();
        Direction shootDir = shooterTank.getDirection();
        
        Position checkPos = currentPos;
        for (int i = 1; i <= GameConfig.getShootRange(); i++) {
            checkPos = checkPos.move(shootDir);
            
            if (!board.isValidPosition(checkPos) || board.hasObstacle(checkPos)) {
                logger.log(String.format("Shot from %s hit %s at (%d,%d)", 
                    shooter.getName(),
                    board.hasObstacle(checkPos) ? "obstacle" : "wall",
                    checkPos.getX(), checkPos.getY()));
                break;
            }
            
            Tank targetTank = board.getTankAt(checkPos);
            if (targetTank != null && targetTank != shooterTank) {
                int oldHealth = targetTank.getHealth();
                targetTank.damage(GameConfig.getDamagePerShot());
                logger.log(String.format("%s hit %s for %d damage (health: %d -> %d)", 
                    shooter.getName(),
                    targetTank.getName(),
                    GameConfig.getDamagePerShot(),
                    oldHealth,
                    targetTank.getHealth()));

                if (targetTank.isDestroyed()) {
                    // Count how many tanks are still alive and find the last one
                    Player lastAlivePlayer = null;
                    int aliveTanks = 0;
                    
                    for (Player p : players) {
                        if (!p.getTank().isDestroyed()) {
                            aliveTanks++;
                            lastAlivePlayer = p;
                        }
                    }
                    
                    logger.log(targetTank.getName() + " was destroyed!");
                    
                    // Only end game if there's exactly one tank left
                    if (aliveTanks == 1) {
                        gameOver = true;
                        logger.log(lastAlivePlayer.getName() + " is the last tank standing!");
                    } else {
                        logger.log(aliveTanks + " tanks still remaining.");
                    }
                }
                break;
            }
        }
    }

    public boolean isGameOver() {
        return turnCount >= maxTurns || gameOver;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean isStalemate() {
        if (!GameConfig.isStalemateDetectionEnabled()) {
            return false;
        }

        if (previousStates.size() < GameConfig.getTurnsToCheck()) {
            return false;
        }

        // Get the most recent state
        GameState lastState = previousStates.getLast();
        
        // Count how many times this exact state has occurred
        int repeatCount = 0;
        int unchangedTurns = 0;
        
        for (int i = previousStates.size() - 1; i >= 0; i--) {
            GameState state = previousStates.get(i);
            if (state.equals(lastState)) {
                repeatCount++;
                unchangedTurns++;
            } else {
                unchangedTurns = 0;
            }
            
            // If we've seen this exact state too many times, or no changes for too long
            if (repeatCount >= GameConfig.getMaxStateRepetitions() || unchangedTurns >= GameConfig.getMaxUnchangedTurns()) {
                logger.log(String.format("Stalemate detected: State repeated %d times, unchanged for %d turns", 
                    repeatCount, unchangedTurns));
                return true;
            }
        }
        return false;
    }

    // Update GameState to include more state information
    private static class GameState {
        private final List<TankState> tankStates;
        private final int turnNumber;

        public GameState(List<Player> players) {
            this.tankStates = new ArrayList<>();
            for (Player player : players) {
                Tank tank = player.getTank();
                tankStates.add(new TankState(
                    tank.getPosition(),
                    tank.getDirection(),
                    tank.getHealth()
                ));
            }
            this.turnNumber = tankStates.hashCode(); // Use as a unique identifier for this state
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameState gameState = (GameState) o;
            
            // Compare tank states
            if (tankStates.size() != gameState.tankStates.size()) {
                return false;
            }
            
            // Check if all tanks are in the same state
            for (int i = 0; i < tankStates.size(); i++) {
                if (!tankStates.get(i).equals(gameState.tankStates.get(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            return turnNumber;
        }
    }

    // Make TankState comparison more precise
    private static class TankState {
        private final Position position;
        private final Direction direction;
        private final int health;

        public TankState(Position position, Direction direction, int health) {
            this.position = position;
            this.direction = direction;
            this.health = health;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TankState tankState = (TankState) o;
            
            // Compare all state components
            return health == tankState.health &&
                   position.getX() == tankState.position.getX() &&
                   position.getY() == tankState.position.getY() &&
                   direction == tankState.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                position.getX(),
                position.getY(),
                direction,
                health
            );
        }
    }
} 