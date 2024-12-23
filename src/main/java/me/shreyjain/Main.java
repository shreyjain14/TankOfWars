package me.shreyjain;

import me.shreyjain.bot.ExampleBot;
import me.shreyjain.engine.GameEngine;
import me.shreyjain.model.Player;
import me.shreyjain.tournament.TournamentManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        
        // runSimpleGame();
        runTournament();
        
    }

    private static void runSimpleGame() {
        Player bot1 = new ExampleBot("Bot1");
        Player bot2 = new ExampleBot("Bot2");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String logFilePath = "logs/game_" + timestamp + ".log";
        GameEngine gameEngine = new GameEngine(List.of(bot1, bot2), logFilePath);

        while (!gameEngine.isGameOver()) {
            gameEngine.executeNextTurn();
        }
    }

    private static void runTournament() {
        try {
            List<Player> bots = List.of(
                new ExampleBot("Bot1"),
                new ExampleBot("Bot2"),
                new ExampleBot("Bot3"),
                new ExampleBot("Bot4")
            );
            TournamentManager tournamentManager = new TournamentManager(bots);
            tournamentManager.runTournament();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}