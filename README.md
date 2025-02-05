# Tank of War

This project is a simple game engine for a tank of war game. 

It is designed to be used for a hackathon. In this game, the player is a tank and the objective is to survive the longest.

This game is where you can create your own bot and compete against other bots.

## Running the Game

1. Clone the repository
2. Build with Maven
3. Run `Main.java` or if you are using termimal to run use the following command `./mvnw clean compile exec:java "-Dexec.mainClass=me.shreyjain.Main"`
4. Check `logs/` for game details

## Logging

All game events are logged to:
- `logs/game_YYYY-MM-DD_HH-mm-ss.log` (individual games)
- `tournament_logs/Tournament_YYYY-MM-DD_HH-mm-ss/` (tournament games)

## For Hackathon Participants

1. Fork this repository
2. Create your bot by extending the 
[`BaseBot`](src/main/java/me/shreyjain/bot/BaseBot.java)
(Take a look at 
[`ExampleBot`](src/main/java/me/shreyjain/bot/extentions/ExampleBot.java) or 
[`ExampleKotlinBot`](src/main/kotlin/me/shreyjain/bot/extensions/ExampleKotlinBot.kt))
3. Test against the
[`ExampleBot`](src/main/java/me/shreyjain/bot/extentions/ExampleBot.java) or
[`ExampleKotlinBot`](src/main/kotlin/me/shreyjain/bot/extensions/ExampleKotlinBot.kt)
4. Submit your bot for the tournament

**See [HACKATHON.md](HACKATHON.md) for detailed instructions on creating and submitting your bot.**

## Kotlin Support

You can also create bots in Kotlin. Ensure your environment is set up to compile Kotlin code.
Take a look at the
[`ExampleKotlinBot`](src/main/kotlin/me/shreyjain/bot/extensions/ExampleKotlinBot.kt)
for an example.

## Tournament Features

- **Automated Matchmaking**: Bots are automatically paired for matches.
- **Leaderboard**: Real-time leaderboard updates based on match results.
- **Scoring System**: Points are awarded based on performance in matches.

## Technical Details

- Java 23+ required
- Uses SnakeYAML for configuration
- ANSI terminal support for visualization
- Maven for dependency management

## License

[MIT License](LICENSE)

## Contributing

This is a hackathon project - contributions and improvements are welcome!