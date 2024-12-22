package me.shreyjain;

import me.shreyjain.engine.GameEngine;
import me.shreyjain.model.Player;
import me.shreyjain.bot.ExampleBot;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create players (bots)
        Player player1 = new ExampleBot("Bot1");
        Player player2 = new ExampleBot("Bot2");
//        Player player3 = new ExampleBot("Bot3");
//        Player player4 = new ExampleBot("Bot4");

        List<Player> players = Arrays.asList(player1, player2);
//        List<Player> players = Arrays.asList(player1, player2, player3, player4);

        // Initialize game engine
        GameEngine gameEngine = new GameEngine(players);

        // Game loop
        while (!gameEngine.isGameOver()) {
            gameEngine.executeNextTurn();
        }

        // Announce winner
        System.out.println("Game Over!");
    }
}