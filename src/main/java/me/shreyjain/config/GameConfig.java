package me.shreyjain.config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class GameConfig {
    private static final Map<String, Object> config;

    static {
        try (InputStream inputStream = GameConfig.class
                .getClassLoader()
                .getResourceAsStream("game-config.yml")) {
            
            Yaml yaml = new Yaml();
            config = yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load game configuration", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getValue(String... keys) {
        Map<String, Object> current = config;
        for (int i = 0; i < keys.length - 1; i++) {
            current = (Map<String, Object>) current.get(keys[i]);
        }
        return (T) current.get(keys[keys.length - 1]);
    }

    public static int getBoardSize() {
        return getValue("board", "size");
    }

    public static int getMinPlayers() {
        return getValue("players", "min");
    }

    public static int getMaxPlayers() {
        return getValue("players", "max");
    }

    public static int getMovesPerTurn() {
        return getValue("players", "movesPerTurn");
    }

    public static double getObstacleDensity() {
        return getValue("board", "obstacleDensity");
    }

    public static int getInitialTankHealth() {
        return getValue("tank", "initialHealth");
    }

    public static int getShootRange() {
        return getValue("tank", "shootRange");
    }

    public static int getDamagePerShot() {
        return getValue("tank", "damagePerShot");
    }

    public static boolean isFogOfWarEnabled() {
        return getValue("board", "fogOfWar", "enabled");
    }

    public static int getFogOfWarViewRadius() {
        return getValue("board", "fogOfWar", "viewRadius");
    }

    // View settings
    public static boolean isViewEnabled() {
        return getValue("view", "enabled");
    }

    public static boolean shouldClearScreen() {
        return getValue("view", "clearScreen");
    }

    public static int getMoveDelay() {
        return getValue("view", "moveDelay");
    }

    public static boolean shouldShowHealth() {
        return getValue("view", "showHealth");
    }

    public static boolean isColorEnabled() {
        return getValue("view", "colorEnabled");
    }
} 