# Hackathon Guide: Creating Your Bot

Welcome to the hackathon! This guide will help you create your own bot using the `BaseBot` class and integrate it into the tournament system.

## Step 1: Understanding the Bot Structure

### BaseBot Class

The `BaseBot` class is an abstract class that implements the `Player` interface. It provides the basic structure for your bot, including a `Tank` object and a name.

```java
public abstract class BaseBot implements Player {
    protected String name;
    protected Tank tank;

    protected BaseBot(String name) {
        this.name = name;
        this.tank = new Tank(name);
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
```

### Implementing Your Bot

To create your bot, extend the `BaseBot` class and implement the `getNextMoves` method. This method should return a list of moves your bot will execute each turn.

```java
public class MyBot extends BaseBot {
    public MyBot(String name) {
        super(name);
    }

    @Override
    public List<Move> getNextMoves(BoardState boardState) {
        // Implement your bot's strategy here
        List<Move> moves = new ArrayList<>();
        // Example: Add logic to decide moves
        return moves;
    }
}
```

## Step 2: Placing Your Bot

1. **Create Your Bot Class**: Place your bot class in the `src/main/java/me/shreyjain/bot/extensions` directory.
2. **Name Your Bot**: Ensure your bot class has a unique name to avoid conflicts with other participants.

## Step 3: Testing Your Bot

1. **Modify `Main.java`**: Add your bot to the list of players in the `runSimpleGame` or `runTournament` method.

```java
Player myBot = new MyBot("MyBot");
List<Player> bots = List.of(
    myBot,
    new ExampleBot("ExampleBot")
    // Add more bots as needed
);
```

2. **Run the Game**: Execute the `Main` class to test your bot against others.

## Step 4: Submitting Your Bot

Once you are satisfied with your bot's performance, submit your bot class file as per the hackathon submission guidelines. (TBD)

## Additional Resources

- **Game Rules**: Refer to the `game-config.yml` for game settings and rules.
- **Logging**: Check the logs in the `logs` or `tournament_logs` directory for detailed game events.

Good luck, and may the best bot win!