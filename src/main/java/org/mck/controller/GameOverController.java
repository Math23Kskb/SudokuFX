package org.mck.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;

public class GameOverController {

    @FXML
    private javafx.scene.control.Button startNewGameButton;

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    public void startNewGameButtonClicked(ActionEvent event) throws IOException {
        if (gameController != null) {
            gameController.resetGame();
        }

        Stage gameOverStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        gameOverStage.close();
    }
}
