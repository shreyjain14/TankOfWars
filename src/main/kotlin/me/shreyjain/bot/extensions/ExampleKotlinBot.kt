package me.shreyjain.bot.extensions

import me.shreyjain.bot.BaseBot
import me.shreyjain.model.BoardState
import me.shreyjain.model.Move

class ExampleKotlinBot(name: String) : BaseBot(name) {

    override fun getNextMoves(boardState: BoardState): List<Move> {
        val moves = mutableListOf<Move>()

        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)
        moves.add(Move.MOVE_FORWARD)

        return moves
    }
} 