# Hackathon Guide: Creating Your Bot

Welcome to the hackathon! This guide will help you create your own bot using the `BaseBot` class and integrate it into the tournament system.

## Step 1: Understanding the Bot Structure

### BaseBot Class

The
[`BaseBot`](src/main/java/me/shreyjain/bot/BaseBot.java)
class is an abstract class that implements the
[`Player`](src/main/java/me/shreyjain/engine/Player.java)
interface. It provides the basic structure for your bot, including a
[`Tank`](src/main/java/me/shreyjain/engine/Tank.java)
object and a name.

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

To create your bot, extend the
[`BaseBot`](src/main/java/me/shreyjain/bot/BaseBot.java)
class and implement the `getNextMoves` method.
This method should return a list of moves your bot will execute each turn.

```java
public class MyBot extends BaseBot {
    public MyBot() {
        super("YourBotName"); // Avoid Adding Spaces to your name
    }

    @Override
    public List<Move> getNextMoves(BoardState boardState) {
        // Implement your bot's strategy here
        List<Move> moves = new ArrayList<>();
        // Example: Add logic to decide moves
        // Only first N moves will execute depending
        // on the `game-config.yml`
        return moves;
    }
}
```

## Step 2: Placing Your Bot

1. **Create Your Bot Class**: Place your bot class in the `src/main/java/me/shreyjain/bot/extensions` directory.
2. **Name Your Bot**: Ensure your bot class has a unique name to avoid conflicts with other participants.

## Step 3: Testing Your Bot

1. **Modify `Main.java`**: Create a new Instance of your bot (at line 30)

```java
// Create new Bots here

// new ExampleBot("ExampleBot")
// new KotlinBot("KotlinBot")

// add your bot here
new TeamABCBot();

```

2. **Run the Game**:

- Execute the `Main` class to test your bot against others.
- If you are using termimal to run use the following command `./mvnw clean compile exec:java "-Dexec.mainClass=me.shreyjain.Main"`

## Step 4: Submitting Your Bot

Once you are satisfied with your bot's performance, submit your bot class file as per the hackathon submission guidelines. (TBD)

## Additional Resources

- **Game Rules**: Refer to the `game-config.yml` for game settings and rules.
- **Logging**: Check the logs in the `logs` or `tournament_logs` directory for detailed game events.

Good luck, and may the best bot win!
