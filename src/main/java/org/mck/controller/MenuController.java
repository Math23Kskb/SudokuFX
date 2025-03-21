package org.mck.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.mck.generator.*;
import org.mck.model.Board;
import org.mck.model.SudokuBoard;
import org.mck.solver.SudokuSolver;

import java.io.IOException;
import java.util.Objects;

public class MenuController {

    @FXML
    private Button startGameButton;

    @FXML
    public void startNewGame() throws IOException {

        Board board = new SudokuBoard();

        BoardGenerator boardGenerator = new SudokuGenerator(
                new SudokuInitializer(
                        new SudokuSolver()), new SudokuRemover(50)
        );

        GameController gameController = new GameController(board, boardGenerator);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));

        loader.setController(gameController);

        GridPane boardGridFromFXML;

        Scene gameScene = new Scene(loader.load());
        gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

        boardGridFromFXML = (GridPane) gameScene.getRoot();
        SudokuBoardView boardView = new SudokuBoardView(boardGridFromFXML, board, gameController);
        gameController.setBoardView(boardView);

        Stage stage = (Stage) startGameButton.getScene().getWindow();
        stage.setScene(gameScene);
        stage.show();
    }
}