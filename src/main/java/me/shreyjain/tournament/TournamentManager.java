package me.shreyjain.tournament;

import me.shreyjain.engine.GameEngine;
import me.shreyjain.model.Player;
import me.shreyjain.util.GameLogger;
import me.shreyjain.config.GameConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TournamentManager {
    private final List<Player> bots;
    private final String tournamentDir;
    private final Connection connection;
    private final GameLogger tournamentLogger;

    public TournamentManager(List<Player> bots) throws SQLException {
        this.bots = bots;
        this.tournamentDir = createTournamentDirectory();
        this.connection = setupDatabase();
        this.tournamentLogger = new GameLogger(tournamentDir + "/tournament.log");
    }

    private String createTournamentDirectory() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String dirName = "tournament_logs/Tournament_" + timestamp;
        Path path = Paths.get(dirName);
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create tournament directory", e);
        }
        return dirName;
    }

    private Connection setupDatabase() throws SQLException {
        String url = "jdbc:sqlite:" + tournamentDir + "/tournament.db";
        Connection conn = DriverManager.getConnection(url);
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS results (" +
                         "bot_name TEXT PRIMARY KEY, " +
                         "matches_played INTEGER, " +
                         "won INTEGER, " +
                         "draw INTEGER, " +
                         "lost INTEGER, " +
                         "points INTEGER)";
            stmt.execute(sql);
        }
        return conn;
    }

    public void runTournament() throws SQLException {
        int repetitions = GameConfig.getRoundRobinRepetitions();
        for (int i = 0; i < bots.size(); i++) {
            for (int j = i + 1; j < bots.size(); j++) {
                for (int k = 0; k < repetitions; k++) {
                    if (k < 2) {
                        runMatch(bots.get(i), bots.get(j), k + 1, true);  // Bot1 starts
                    } else if (k < 4) {
                        runMatch(bots.get(j), bots.get(i), k + 1, true);  // Bot2 starts
                    } else {
                        runMatch(bots.get(i), bots.get(j), k + 1, false); // Random start
                    }
                }
            }
        }
        tournamentLogger.close();
    }

    private void runMatch(Player bot1, Player bot2, int matchNumber, boolean fixedOrder) throws SQLException {
        String logFilePath = String.format("%s/match_%s_vs_%s_game_%d.log", 
                                           tournamentDir, bot1.getName(), bot2.getName(), matchNumber);
        List<Player> players = fixedOrder ? List.of(bot1, bot2) : (Math.random() < 0.5 ? List.of(bot1, bot2) : List.of(bot2, bot1));
        GameEngine gameEngine = new GameEngine(players, logFilePath);

        while (!gameEngine.isGameOver()) {
            gameEngine.executeNextTurn();
        }

        // Determine the match result
        String winnerName = null;
        if (gameEngine.isStalemate()) {
            winnerName = "Draw";
        } else {
            winnerName = gameEngine.getCurrentPlayer().getName();
        }
        updateResults(bot1, bot2, winnerName);

        // Log match result
        String result = String.format("Match %d between %s and %s: ", matchNumber, bot1.getName(), bot2.getName());
        if ("Draw".equals(winnerName)) {
            result += "Draw";
        } else {
            result += winnerName + " won";
        }
        System.out.println(result);
        tournamentLogger.log(result);
    }

    private void updateResults(Player bot1, Player bot2, String winnerName) throws SQLException {
        if ("Draw".equals(winnerName)) {
            updateBotStats(bot1.getName(), 0, 1, 0, 1);
            updateBotStats(bot2.getName(), 0, 1, 0, 1);
        } else if (winnerName.equals(bot1.getName())) {
            updateBotStats(bot1.getName(), 1, 0, 0, 3);
            updateBotStats(bot2.getName(), 0, 0, 1, 0);
        } else if (winnerName.equals(bot2.getName())) {
            updateBotStats(bot2.getName(), 1, 0, 0, 3);
            updateBotStats(bot1.getName(), 0, 0, 1, 0);
        }
    }

    private void updateBotStats(String botName, int won, int draw, int lost, int points) throws SQLException {
        String sql = "INSERT INTO results (bot_name, matches_played, won, draw, lost, points) " +
                     "VALUES (?, 1, ?, ?, ?, ?) " +
                     "ON CONFLICT(bot_name) DO UPDATE SET " +
                     "matches_played = matches_played + 1, " +
                     "won = won + ?, " +
                     "draw = draw + ?, " +
                     "lost = lost + ?, " +
                     "points = points + ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, botName);
            pstmt.setInt(2, won);
            pstmt.setInt(3, draw);
            pstmt.setInt(4, lost);
            pstmt.setInt(5, points);
            pstmt.setInt(6, won);
            pstmt.setInt(7, draw);
            pstmt.setInt(8, lost);
            pstmt.setInt(9, points);
            pstmt.executeUpdate();
        }
    }
} 