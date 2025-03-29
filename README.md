# SudokuFX

[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java Version](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

SudokuFX is a JavaFX-based Sudoku game providing a classic and engaging puzzle experience with a clean user interface and core Sudoku logic.

## Features

*   **Interactive Sudoku Board:** A 9x9 grid-based Sudoku board where players can enter numbers and attempt to solve the puzzle. The board is visually styled with subgrid highlighting for improved readability.
*   **Number Input Validation:** Real-time validation prevents the placement of numbers that violate Sudoku rules. Invalid moves trigger a warning alert explaining the specific rule violation (row, column, or 3x3 box conflict).
*   **Automatic Sudoku Generation:** Automatically generates new Sudoku puzzles with varying difficulty.
    *   A Sudoku solver creates a complete, valid Sudoku solution.
    *   A number remover removes numbers from the solved board to create a puzzle, balancing difficulty and solvability.
*   **Game Over Detection:** Detects when the Sudoku board is completely filled. A "Congratulations!" screen appears upon completion.
*   **New Game Functionality:** A "Start New Game" button on the "Congratulations!" screen starts a new Sudoku game.
*   **Unit and System Tests:** Comprehensive unit and system tests ensure the software's core functionalities are robust.

## Technologies Used

*   [Java 17](https://adoptium.net/temurin/releases/?os=windows&version=17) or higher
*   [JavaFX](https://openjfx.io/) for the user interface
*   [Maven](https://maven.apache.org/) for build management
*   [JUnit 5](https://junit.org/junit5/) and [Mockito](https://site.mockito.org/) for unit testing
*   [TestFX](http://testfx.github.io/) for UI testing
*   [ControlsFX](https://www.controlsfx.org/) for additional UI controls

## Installation

There are two primary ways to install and run SudokuFX:

**1. Using the JAR file (Simplest):**

*   Download the `SudokuFX-1.0.0.jar` file from the [Releases Page](https://github.com/[Your GitHub Username]/SudokuFX/releases/tag/v1.0.0) (or the latest release).
*   Ensure you have Java 17 or higher installed on your system.
*   Run the game by double-clicking the JAR file (or using `java -jar SudokuFX-1.0.0.jar` in your command line/terminal).

**2. Building from Source (For Developers):**

1.  Clone the repository:
    ```bash
    git clone https://github.com/[Your GitHub Username]/SudokuFX.git
    cd SudokuFX
    ```
2.  Build the project using Maven:
    ```bash
    mvn clean install
    ```
3.  Run the game:
    ```bash
    mvn javafx:run
    ```
    Alternatively, you can find the JAR file in the `target/` directory after building and run it as described in method 1.

## Usage

Simply launch the application. The Sudoku board will appear, and you can start playing by entering numbers into the empty cells.  The game will provide real-time validation of your moves.  When you complete the puzzle, you'll be congratulated!

## Contributing

We welcome contributions from the community! If you'd like to contribute to SudokuFX, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes.
4.  Write tests to cover your changes.
5.  Submit a pull request.

Please ensure your code adheres to the project's coding style and that all tests pass before submitting a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Known Issues

*   No known issues at this time.  Please report any issues you encounter on the [Issue Tracker](https://github.com/Math23Kskb/SudokuFX/issues).

## Future Implementations

We plan to add the following features in future releases:

*   **Difficulty Selection:** Allow users to choose a specific difficulty level (Easy, Medium, Hard, Expert) to customize their gameplay experience.
*   **Timer:** Implement a timer to track how long it takes the user to solve a puzzle.
*   **Hints:** Provide a "Hint" button that reveals a single correct number on the board, offering assistance to players who are stuck.
*   **Undo/Redo:** Implement undo and redo functionality to allow players to easily revert and reapply moves.
*   **Save/Load Game:** Enable users to save their progress and load games later.
*   **Improved UI/UX:** Enhance the user interface and overall user experience with improved styling, animations, and accessibility features.
*   **More robust error handling:** Add robust error handling for a better user experience.

## Credits

Developed by Matheos Chiyuki Kusakabe (Math23Kskb)

## Support

If you have any questions or need help with SudokuFX, please open an issue on the [Issue Tracker](https://github.com/Math23Kskb/SudokuFX/issues).

Enjoy playing Sudoku! We hope you find SudokuFX to be a fun and engaging puzzle game.
