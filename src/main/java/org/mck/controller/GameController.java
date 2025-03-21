package org.mck.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.mck.generator.BoardGenerator;
import org.mck.model.Board;

import java.io.IOException;
import java.util.Objects;

public class GameController {
    private final Board board;
    private final BoardGenerator boardGenerator;
    private SudokuBoardView boardView;
    private Stage gameOverStage;
    private FXMLLoader fxmlLoader;

    public GameController(Board board, BoardGenerator boardGenerator) {
        this.board = board;
        this.boardGenerator = boardGenerator;
    }

    public void setBoardView(SudokuBoardView boardView) {
        this.boardView = boardView;

        initializeGame();
    }

    public void setStageForGameOver(Stage stage) {
        this.gameOverStage = stage;
    }

    public Board getBoard() {
        return board;
    }

    public SudokuBoardView getBoardView() {
        return boardView;
    }

    public void setFxmlLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    @FXML
    private GridPane boardGrid;

    private void initializeGame() {
        boardGenerator.generateBoard(board);
        boardView.initializeBoardDisplay();
    }

    public boolean isValidSudokuMove(int row, int col, int num) {
        return board.isValidMove(row, col, num);
    }

    public void showInvalidMoveAlert(int num, int row, int col) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText("Sudoku Rule Violation!");
        StringBuilder message = new StringBuilder(String.format("Number %d cannot be placed here because it violates Sudoku Rules\n", num));

        if (board.isNumberInRow(row, num)) {
            message.append("- It's already in row ").append(row + 1).append("\n");
        }
        if (board.isNumberInColumn(col, num)) {
            message.append("- It's already in column ").append(col + 1).append("\n");
        }
        if (board.isNumberInBox(row, col, num)) {
            message.append("- It's already in the 3x3 block containing this cell.");
        }

        alert.setContentText(message.toString());
        alert.showAndWait();
    }

    public void GameOver(ActionEvent actionEvent) throws IOException {
        if (board.isBoardComplete()) {
            showGameOverWindow(actionEvent);
        } else {
            System.out.println("Board is NOT complete yet.");
        }
    }

    void showGameOverWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameOverView.fxml"));
        Stage gameOverStage = new Stage();
        Scene gameOverScene = new Scene(loader.load());
        gameOverScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        GameOverController gameOverController = loader.getController();
        gameOverController.setGameController(this); // Pass GameController instance

        gameOverStage.setTitle("Congratulations!");
        gameOverStage.setScene(gameOverScene);
        gameOverStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        gameOverStage.show();
    }

}
