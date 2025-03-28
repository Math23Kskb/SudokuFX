package org.mck.generator;

import org.mck.model.Board;

public class SudokuGenerator implements BoardGenerator {
    private final BoardInitializer initializer;
    private final BoardRemover remover;

    public SudokuGenerator(BoardInitializer initializer, BoardRemover remover) {
        this.initializer = initializer;
        this.remover = remover;
    }

    @Override
    public void generateBoard(Board board) {
        initializer.initializeBoard(board);

        board.deepCopy();

        remover.removeNumbers(board);
    }
}