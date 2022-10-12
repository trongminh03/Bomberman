package uet.oop.bomberman.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.GameButton;
import uet.oop.bomberman.model.GameSubScene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuViewManager {
    private AnchorPane menuPane;
    private static Scene menuScene;
    private Stage menuStage;

    AudioManager menuSong = new AudioManager("res/audio/menu_song.mp3");

    private final static int MENU_BUTTON_START_X = 30;
    private final static int MENU_BUTTON_START_Y = 80;

    List<Button> menuButtons;

    private GameSubScene creditsSubScene;
    private GameSubScene helpSubScene;
    private GameSubScene scoreSubScene;

    private GameSubScene sceneToHide;

//    boolean soundOn = true;
//    ImageView speaker = new ImageView();
//    Image speakerOn = new Image("/model/sound-on.png");
//    Image silent = new Image("/model/silent.png");


    public MenuViewManager() {
        menuButtons = new ArrayList<Button>();
        menuPane = new AnchorPane();
        menuScene = new Scene(menuPane, Sprite.SCALED_SIZE * GameViewManager.WIDTH,
                                Sprite.SCALED_SIZE * GameViewManager.HEIGHT);
        menuStage = new Stage();
        menuStage.setScene(menuScene);
        createBackground();
        createLogo();
        createSubScene();
        createButton();
        createSoundButton();
        if (AudioManager.isSoundEnabled()) {
            playMenuMusic();
        } else {
            menuSong.stop();
        }
    }

    public Stage getMenuStage() {
        return menuStage;
    }

    public Scene getMenuScene() {
        return menuScene;
    }

    public void playMenuMusic() {
        menuSong.play(MediaPlayer.INDEFINITE);
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

    private void showSubScene(GameSubScene subScene) {
        if (sceneToHide != null) {
            sceneToHide.moveSubScene();
        }

        subScene.moveSubScene();
        sceneToHide = subScene;
    }

    private void createSubScene() {
        creditsSubScene = new GameSubScene();
        menuPane.getChildren().add(creditsSubScene);

        helpSubScene = new GameSubScene();
        menuPane.getChildren().add(helpSubScene);

        scoreSubScene = new GameSubScene();
        menuPane.getChildren().add(scoreSubScene);
    }

    private void createSoundButton() {
        ImageView speaker = new ImageView();
        Image speakerOn = new Image("/model/sound-on.png");
        Image silent = new Image("/model/silent.png");
        if (AudioManager.isSoundEnabled()) {
            speaker.setImage(speakerOn);
        } else {
            speaker.setImage(silent);
        }
        speaker.setLayoutX(100);
        speaker.setLayoutY(30);

        speaker.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (AudioManager.isSoundEnabled()) {
                    speaker.setImage(silent);
                    AudioManager.setSoundOn(false);
                    menuSong.stop();
                } else {
                    speaker.setImage(speakerOn);
                    AudioManager.setSoundOn(true);
                    menuSong.play(MediaPlayer.INDEFINITE);
                }
            }
        });
        menuPane.getChildren().add(speaker);
    }

    private void createStartButton() {
        GameButton startButton = new GameButton("PLAY GAME");
        addMenuButton(startButton, 0);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameViewManager gameView = new GameViewManager();
//                gameView.createNewGame();
                menuSong.stop();
                menuStage.close();
                BombermanGame.switchScene(gameView.getScene());
            }
        });
    }

    private void createScoreButton() {
        GameButton scoreButton = new GameButton("SCORES");
        addMenuButton(scoreButton, 1);

        scoreButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(scoreSubScene);
            }
        });
    }

    private void createHelpButton() {
        GameButton helpButton = new GameButton("HELP");
        addMenuButton(helpButton, 2);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(helpSubScene);
            }
        });
    }

    private void createCreditsButton() {
        GameButton creditsButton = new GameButton("CREDITS");
        addMenuButton(creditsButton, 3);

        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(creditsSubScene);
            }
        });
    }

    private void createLogo() {
        ImageView logo = new ImageView("/model/logo.png");
        logo.setLayoutX(280);
        logo.setLayoutY(5);

        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logo.setEffect(new DropShadow());
            }
        });

        logo.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logo.setEffect(null);
            }
        });

        menuPane.getChildren().add(logo);
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
