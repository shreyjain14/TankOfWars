package me.shreyjain.bot;

import me.shreyjain.config.GameConfig;
import me.shreyjain.model.*;
import java.util.ArrayList;
import java.util.List;

public class ExampleBot implements Player {
    private final String name;
    private final Tank tank;
    private final Position centerPosition;
    private int stuckCounter = 0;

    public ExampleBot(String name) {
        this.name = name;
        this.tank = new Tank(GameConfig.getInitialTankHealth(), name);
        this.centerPosition = new Position(
            GameConfig.getBoardSize() / 2,
            GameConfig.getBoardSize() / 2
        );
    }

    @Override
    public List<Move> getNextMoves(BoardState boardState) {
        List<Move> moves = new ArrayList<>();
        Position currentPos = tank.getPosition();
        Direction currentDirection = tank.getDirection();

        // First priority: Shoot if enemy is in sight
        Tank enemyTank = findEnemyTankInSight(boardState);
        if (enemyTank != null) {
            // Rotate to face enemy if needed
            Direction directionToEnemy = getDirectionToTarget(currentPos, enemyTank.getPosition());
            while (currentDirection != directionToEnemy && moves.size() < GameConfig.getMovesPerTurn()) {
                moves.add(Move.ROTATE_RIGHT);
                currentDirection = currentDirection.rotateRight();
            }
            
            // Shoot if facing enemy
            if (currentDirection == directionToEnemy && moves.size() < GameConfig.getMovesPerTurn()) {
                moves.add(Move.SHOOT);
                stuckCounter = 0;
                return moves;
            }
        }

        // Second priority: Move towards center if not there
        if (!currentPos.equals(centerPosition) && moves.size() < GameConfig.getMovesPerTurn()) {
            // Calculate direction to center
            Direction directionToCenter = getDirectionToTarget(currentPos, centerPosition);

            // Check if we can move in the desired direction
            Position nextPos = currentPos.move(directionToCenter);
            if (!canMoveTo(boardState, nextPos)) {
                stuckCounter++;
                // If stuck for too long, try alternative directions
                if (stuckCounter > 2) {
                    // Try moving in a different direction
                    for (Direction dir : Direction.values()) {
                        nextPos = currentPos.move(dir);
                        if (canMoveTo(boardState, nextPos)) {
                            directionToCenter = dir;
                            break;
                        }
                    }
                    // If still stuck, just rotate
                    if (!canMoveTo(boardState, nextPos)) {
                        moves.add(Move.ROTATE_RIGHT);
                        stuckCounter = 0;
                        return moves;
                    }
                }
            } else {
                stuckCounter = 0;
            }

            // Rotate towards target direction if needed
            while (currentDirection != directionToCenter && moves.size() < GameConfig.getMovesPerTurn()) {
                moves.add(Move.ROTATE_RIGHT);
                currentDirection = currentDirection.rotateRight();
            }

            // Move forward if facing the right direction and path is clear
            if (currentDirection == directionToCenter && moves.size() < GameConfig.getMovesPerTurn()) {
                Position forwardPos = currentPos.move(currentDirection);
                if (canMoveTo(boardState, forwardPos)) {
                    moves.add(Move.MOVE_FORWARD);
                }
            }
        }

        return moves;
    }

    private boolean canMoveTo(BoardState boardState, Position position) {
        return boardState.isValidPosition(position) && 
               !boardState.hasObstacle(position) && 
               boardState.getTankAt(position) == null;
    }

    private Tank findEnemyTankInSight(BoardState boardState) {
        Position currentPos = tank.getPosition();
        Direction currentDirection = tank.getDirection();
        int range = GameConfig.getShootRange();

        // Check cells in front of tank up to shoot range
        Position checkPos = currentPos;
        for (int i = 1; i <= range; i++) {
            checkPos = checkPos.move(currentDirection);
            if (!boardState.isValidPosition(checkPos)) {
                break;
            }
            
            if (boardState.hasObstacle(checkPos)) {
                break;
            }
            
            Tank tankAtPosition = boardState.getTankAt(checkPos);
            if (tankAtPosition != null && tankAtPosition != this.tank) {
                // Only return the tank if it's directly in our line of fire
                switch (currentDirection) {
                    case NORTH:
                        if (tankAtPosition.getPosition().getX() == currentPos.getX() && 
                            tankAtPosition.getPosition().getY() < currentPos.getY())
                            return tankAtPosition;
                        break;
                    case SOUTH:
                        if (tankAtPosition.getPosition().getX() == currentPos.getX() && 
                            tankAtPosition.getPosition().getY() > currentPos.getY())
                            return tankAtPosition;
                        break;
                    case EAST:
                        if (tankAtPosition.getPosition().getY() == currentPos.getY() && 
                            tankAtPosition.getPosition().getX() > currentPos.getX())
                            return tankAtPosition;
                        break;
                    case WEST:
                        if (tankAtPosition.getPosition().getY() == currentPos.getY() && 
                            tankAtPosition.getPosition().getX() < currentPos.getX())
                            return tankAtPosition;
                        break;
                }
                break;
            }
        }
        return null;
    }

    private Direction getDirectionToTarget(Position from, Position to) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();

        // If on same column, prioritize vertical movement
        if (dx == 0) {
            return dy > 0 ? Direction.SOUTH : Direction.NORTH;
        }
        // If on same row, prioritize horizontal movement
        else if (dy == 0) {
            return dx > 0 ? Direction.EAST : Direction.WEST;
        }
        // Otherwise, prioritize the larger difference
        else {
            if (Math.abs(dx) > Math.abs(dy)) {
                return dx > 0 ? Direction.EAST : Direction.WEST;
            } else {
                return dy > 0 ? Direction.SOUTH : Direction.NORTH;
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Tank getTank() {
        return tank;
    }
} 