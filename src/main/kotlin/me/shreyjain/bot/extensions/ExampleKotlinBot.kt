package me.shreyjain.bot.extensions

import me.shreyjain.bot.BaseBot
import me.shreyjain.engine.BoardState
import me.shreyjain.engine.Move

class ExampleKotlinBot(name: String) : BaseBot(name) {

    override fun getNextMoves(boardState: BoardState): List<Move> {
        val moves = mutableListOf<Move>()

        // Only the first N MOVE_FORWARD would be executed
        // where N = player.movesPerTurn in the game-config.yml
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)

        return moves
    }
} 