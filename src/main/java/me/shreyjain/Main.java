package me.shreyjain;

import me.shreyjain.bot.extentions.ExampleBot;
import me.shreyjain.bot.extensions.ExampleKotlinBot;
import me.shreyjain.engine.GameEngine;
import me.shreyjain.model.Player;
import me.shreyjain.tournament.TournamentManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        
        runSimpleGame();
//      runTournament();
        
    }

	@SuppressWarnings("unused")
    private static void runSimpleGame() {
        Player bot1 = new ExampleBot("Bot1");
        Player bot2 = new ExampleKotlinBot("KotlinBot");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String logFilePath = "logs/game_" + timestamp + ".log";
        GameEngine gameEngine = new GameEngine(List.of(bot1, bot2), logFilePath);

        while (!gameEngine.isGameOver()) {
            gameEngine.executeNextTurn();
        }
    }

    @SuppressWarnings("unused")
    private static void runTournament() {
        try {
            List<Player> bots = List.of(
                new ExampleBot("Bot1"),
                new ExampleBot("Bot2"),
                new ExampleBot("Bot3"),
//                new ExampleBot("Bot4"),
//                new ExampleBot("Bot5"),
//                new ExampleBot("Bot6"),
//                new ExampleBot("Bot7"),
//                new ExampleBot("Bot8"),
//                new ExampleBot("Bot9"),
//                new ExampleBot("Bot10"),
//                new ExampleBot("Bot11"),
//                new ExampleBot("Bot12"),
//                new ExampleBot("Bot13"),
//                new ExampleBot("Bot14"),
//                new ExampleBot("Bot15"),
//                new ExampleBot("Bot16"),
//                new ExampleBot("Bot17"),
//                new ExampleBot("Bot18"),
//                new ExampleBot("Bot19"),
//                new ExampleBot("Bot20"),
//                new ExampleBot("Bot21"),
//                new ExampleBot("Bot22"),
//                new ExampleBot("Bot23"),
//                new ExampleBot("Bot24"),
                new ExampleBot("Bot25")
            );
            TournamentManager tournamentManager = new TournamentManager(bots);
            tournamentManager.runTournament();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while running the tournament", e);
        }
    }
}