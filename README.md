
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
3. Test against the `ExampleBot`
4. Submit your bot for the tournament

## Technical Details

- Java 8+ required
- Uses SnakeYAML for configuration
- ANSI terminal support for visualization
- Maven for dependency management

## License

[MIT License](LICENSE)

## Contributing

This is a hackathon project - contributions and improvements are welcome!