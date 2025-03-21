package org.mck.controller;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class GameOverControllerTest {

    private GameOverController gameOverController;

    @Mock
    private GameController gameController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameOverController = new GameOverController();
    }

    @Test
    void setGameController_SetsGameControllerField() {
        gameOverController.setGameController(gameController);

        assertEquals(gameController, gameOverController.getGameController(), "GameController should be set");
    }

}
