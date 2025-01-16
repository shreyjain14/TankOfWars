package me.shreyjain;

import me.shreyjain.bot.extentions.ExampleBot;
import me.shreyjain.bot.extensions.ExampleKotlinBot;
import me.shreyjain.config.GameConfig;
import me.shreyjain.engine.GameEngine;
import me.shreyjain.model.Player;
import me.shreyjain.tournament.TournamentManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        // Uncomment this section to run Simple Game with Example Bot
        int currentGamePlayers = GameConfig.getCurrentGamePlayers();

        // Create new Bots here

        // new ExampleBot("ExampleBot")
        // new KotlinBot("KotlinBot")


        // for testing
        for (int i=0; i<currentGamePlayers; i++) {
            new ExampleBot("Bot" + (i+1));
        }

        long startTime = System.nanoTime();

        if (GameConfig.getTournamentEnabled()) {
            runTournament();
        } else {
            runSimpleGame();
        }

        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1000000;

        System.out.println("It took " + executionTime + " ms.");
        logger.config("It took " + executionTime + " ms.");

    }


    private static void runSimpleGame() {

        if (Player.players.size() < GameConfig.getMinPlayers() || Player.players.size() > GameConfig.getMaxPlayers()) {
            throw new IllegalArgumentException("Invalid number of players");
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String logFilePath = "logs/game_" + timestamp + ".log";

        GameEngine gameEngine = new GameEngine(Player.players, logFilePath);

        while (!gameEngine.isGameOver()) {
            gameEngine.executeNextTurn();
        }
    }

    private static void runTournament() {
        try {

            if (Player.players.size() < GameConfig.getMinTournamentPlayers()) {
                throw new IllegalArgumentException("Invalid number of players in the tournament");
            }

            TournamentManager tournamentManager = new TournamentManager(Player.players);
            tournamentManager.runTournament();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while running the tournament", e);
        }
    }
}