package me.shreyjain.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;

public class GameLogger {
    private static final String LOG_DIR = "logs";
    private static final String LATEST_LOG = "logs/latest.log";
    private final PrintWriter logWriter;
    private final String logFile;

    public GameLogger() {
        try {
            // Create logs directory if it doesn't exist
            Path logPath = Paths.get(LOG_DIR).toAbsolutePath();
            System.out.println("Logs will be stored in: " + logPath);
            Files.createDirectories(logPath);
            
            // Create timestamped log file
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            this.logFile = LOG_DIR + "/game_" + timestamp + ".log";
            this.logWriter = new PrintWriter(new FileWriter(logFile));
            
            // Also create/update the latest.log symlink or copy
            updateLatestLog();
            
            // Log game start
            log("=== Game Started at " + timestamp + " ===");
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize game logger", e);
        }
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String logMessage = String.format("[%s] %s", timestamp, message);
        
        // Write to log file
        logWriter.println(logMessage);
        logWriter.flush();
        
        // Update latest.log
        try (PrintWriter latest = new PrintWriter(new FileWriter(LATEST_LOG, true))) {
            latest.println(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLatestLog() throws IOException {
        // Delete existing latest.log if it exists
        Files.deleteIfExists(Paths.get(LATEST_LOG));
        
        // Create new latest.log
        Files.createFile(Paths.get(LATEST_LOG));
    }

    public void close() {
        logWriter.println("=== Game Ended ===");
        logWriter.close();
    }
} 