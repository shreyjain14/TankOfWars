package me.shreyjain.model;

import java.util.List;

public interface Player {
    /**
     * Gets the next moves for the player's tank based on the current board state.
     * Each player can make up to GameConfig.getMovesPerTurn() moves per turn.
     *
     * @param boardState Current state of the game board
     * @return List of moves to execute this turn
     */
    List<Move> getNextMoves(BoardState boardState);

    /**
     * Gets the player's name/identifier
     *
     * @return The name of the player
     */
    String getName();

    /**
     * Gets the player's tank
     *
     * @return The tank controlled by this player
     */
    Tank getTank();
} 