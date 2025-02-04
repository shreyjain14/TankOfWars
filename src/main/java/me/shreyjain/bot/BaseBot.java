package me.shreyjain.bot;

import me.shreyjain.engine.BoardState;
import me.shreyjain.engine.Move;
import me.shreyjain.engine.Player;
import me.shreyjain.engine.Tank;

import java.util.List;

public abstract class BaseBot implements Player {

    protected String name;
    protected Tank tank;

    protected BaseBot (String name) {
        this.name = name;
        this.tank = new Tank(name);

        Player.players.add(this);

    }

    @Override
    public abstract List<Move> getNextMoves(BoardState boardState);

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Tank getTank() {
        return tank;
    }



}
