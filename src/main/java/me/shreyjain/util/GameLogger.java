package me.shreyjain.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;

public class GameLogger {
    private final PrintWriter logWriter;

    public GameLogger(String logFilePath) {
        try {
            // Create directories if they don't exist
            Path logPath = Paths.get(logFilePath).getParent();
            if (logPath != null) {
                Files.createDirectories(logPath);
            }
            
            this.logWriter = new PrintWriter(new FileWriter(logFilePath, true));
            log("=== Game Started at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + " ===");
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize game logger", e);
        }
    }

    public void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        logWriter.println("[" + timestamp + "] " + message);
        logWriter.flush();
    }

    public void close() {
        log("=== Game Ended ===");
        logWriter.close();
    }
} 