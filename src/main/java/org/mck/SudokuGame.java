package org.mck;

import org.mck.model.SudokuBoard;
import org.mck.model.SudokuGenerator;

import java.util.Scanner;

public class SudokuGame {
    private final SudokuBoard board;

    public SudokuGame() {
        this.board = new SudokuBoard();
        new SudokuGenerator(board);
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            board.printBoard();
            System.out.println("Enter row (0-8), column (0-8), and number (1-9) OR -1 to quit:");

            int row = scanner.nextInt();
            if (row == -1) break;
            int col = scanner.nextInt();
            int num = scanner.nextInt();

            if (board.isValidMove(row, col, num)) {
                board.setValue(row, col, num);
            } else {
                System.out.println("⚠️ Invalid move!");
            }
        }
        scanner.close();
    }
}
