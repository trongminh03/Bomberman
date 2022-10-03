package uet.oop.bomberman.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.GameButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuViewManager {
    private AnchorPane menuPane;
    private static Scene menuScene;
    private Stage menuStage;

    private final static int MENU_BUTTON_START_X = 30;
    private final static int MENU_BUTTON_START_Y = 80;

    List<Button> menuButtons;

    public MenuViewManager() {
        menuButtons = new ArrayList<Button>();
        menuPane = new AnchorPane();
        menuScene = new Scene(menuPane, Sprite.SCALED_SIZE * GameViewManager.WIDTH,
                                Sprite.SCALED_SIZE * GameViewManager.HEIGHT);
        menuStage = new Stage();
        menuStage.setScene(menuScene);
        createButton();
        createBackground();
    }

    public Stage getMenuStage() {
        return menuStage;
    }

    public Scene getMenuScene() {
        return menuScene;
    }

    private void addMenuButton(Button button, int index) {
        button.setLayoutX(MENU_BUTTON_START_X);
        button.setLayoutY(MENU_BUTTON_START_Y + index * 100);
        menuButtons.add(button);
        menuPane.getChildren().add(button);
    }

    private void createButton() {
        createStartButton();
        createScoreButton();
        createHelpButton();
        createCreditsButton();
    }

    private void createStartButton() {
        GameButton startButton = new GameButton("PLAY GAME");
        addMenuButton(startButton, 0);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameViewManager gameView = new GameViewManager();
//                gameView.createNewGame();
                menuStage.close();
                BombermanGame.switchScene(gameView.getScene());
            }
        });
    }

    private void createScoreButton() {
        GameButton scoreButton = new GameButton("SCORES");
        addMenuButton(scoreButton, 1);
    }

    private void createHelpButton() {
        GameButton helpButton = new GameButton("HELP");
        addMenuButton(helpButton, 2);
    }

    private void createCreditsButton() {
        GameButton creditsButton = new GameButton("CREDITS");
        addMenuButton(creditsButton, 3);
    }

    private void createBackground() {
        Image backgroundImage = new Image("/model/background.png", 800, 600, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, null,
                        null, BackgroundPosition.DEFAULT, null);
        menuPane.setBackground(new Background(background));
    }

    public static Scene getScene() {
        return menuScene;
    }
}
