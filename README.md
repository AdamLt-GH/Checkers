# Checkers

A checkers game written in java using processing. There's an option to play against another player and three difficulty settings to play against the CPU.

## Features

- Local player vs player mode
- Player vs CPU mode
- Easy, medium and hard CPU difficulties
- Required captures and multiple captures
- King promotion
- Turn display and invalid move feedback
- Game over screen that returns to the menu

## Rules used

- Black moves first.
- Regular pieces move diagonally towards the other side of the board.
- Kings can move diagonally in either direction.
- If a capture is available it has to be taken.
- If the same piece can capture again then the capture has to continue.
- A regular piece becomes a king when it reaches the other end.
- A player loses when they have no pieces or no legal moves left.

 ## Controls

Choose a game mode from the main menu. Select one of your pieces and then select the square you want to move to.
note: in CPU mode, player plays black and CPU plays white. 

## CPU difficulties

- Easy searches 2 moves deep.
- Medium searches 4 moves deep.
- Hard searches 6 moves deep.

Minimax with Alpha-Beta pruning is performed by the CPU. It analyzes the pieces, kings and board positions it would be useful to occupy in selecting a move.


## Running the game
Java 17 is required.

On macOS or Linux:
```bash
./gradlew run
```

On Windows:
```bat
gradlew.bat run
```

## Running the tests

On macOS or Linux:
```bash
./gradlew test
```

On Windows:
```bat
gradlew.bat test
```
