package me.shreyjain.bot;

import me.shreyjain.model.BoardState;
import me.shreyjain.model.Move;
import me.shreyjain.model.Player;
import me.shreyjain.model.Tank;

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
