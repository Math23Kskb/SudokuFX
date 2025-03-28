package org.mck.model;

public interface Board {
    int getValue(int row, int col);
    void setValue(int row, int col, int num);
    boolean isValidMove(int row, int col, int num);
    boolean isCellEmpty(int row, int col);
    int getSize();
    int getSubgridSize();

    boolean isNumberInRow(int row, int num);
    boolean isNumberInColumn(int col, int num);
    boolean isNumberInBox(int row, int col, int num);

    boolean isBoardComplete();

    Board deepCopy();
}
