## Running the Game

1. Clone the repository
2. Build with Maven
3. Run `Main.java`
4. Check `logs/latest.log` for game details

## Logging

All game events are logged to:
- `logs/game_YYYY-MM-DD_HH-mm-ss.log` (individual games)
- `logs/latest.log` (most recent game)

## For Hackathon Participants

1. Fork this repository
2. Create your bot by implementing the `Player` interface
3. Test against the `ExampleBot` or `ExampleKotlinBot`
4. Submit your bot for the tournament

**See [HACKATHON.md](HACKATHON.md) for detailed instructions on creating and submitting your bot.**

## Kotlin Support

You can also create bots in Kotlin. Ensure your environment is set up to compile Kotlin code.
Take a look at the `ExampleKotlinBot` for an example.

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