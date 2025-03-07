package org.mck.generator;

import org.mck.model.Board;

import java.util.Random;

public class SudokuRemover implements BoardRemover {

    private final int numbersToRemove;

    public SudokuRemover(int numbersToRemove) {
        this.numbersToRemove = numbersToRemove;
    }

    @Override
    public void removeNumbers(Board board) {
        Random rand = new Random();
        int count = numbersToRemove;
        while (count > 0) {
            int row = rand.nextInt(board.getSize());
            int col = rand.nextInt(board.getSize());
            if (!board.isCellEmpty(row, col)) {
                board.setValue(row, col, 0);
                count--;
            }
        }
    }
}
