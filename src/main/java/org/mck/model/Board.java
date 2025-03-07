package org.mck.model;

public interface Board {
    int getValue(int row, int col);
    void setValue(int row, int col, int num);
    boolean isValidMove(int row, int col, int num);
    boolean isCellEmpty(int row, int col);
    void resetBoard();
    int getSize(); // Added to get board size
    int getSubgridSize();
}
