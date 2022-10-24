package uet.oop.bomberman.gui;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.GameButton;
import uet.oop.bomberman.model.GameSubScene;
import uet.oop.bomberman.model.InfoLabel;
import uet.oop.bomberman.model.OptionController;

import java.util.ArrayList;
import java.util.List;

public class MenuViewManager {
    private AnchorPane menuPane;
    private static Scene menuScene;
    private Stage menuStage;

    public static AudioManager menuSong = new AudioManager("res/audio/menu_song.mp3", AudioManager.BACKGROUND_MUSIC);

    private final static int MENU_BUTTON_START_X = 30;
    private final static int MENU_BUTTON_START_Y = 40;

    List<Button> menuButtons;
    private GameSubScene creditsSubScene;
    private GameSubScene helpSubScene;
    private GameSubScene optionSubScene;
    private GameSubScene scoreSubScene;
    private OptionController backgroundMusic;
    private OptionController gameplayMusic;

    private GameSubScene sceneToHide;
//    MenuViewManager menu;

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
        playMenuMusic();
//        createSoundButton();
    }

    public Stage getMenuStage() {
        return menuStage;
    }

    public Scene getMenuScene() {
        return menuScene;
    }

    // phải truyền được hàm này vào trong handle của OptionController
    public static void playMenuMusic() {
        if (AudioManager.isSoundEnabled(AudioManager.BACKGROUND_MUSIC)) {
            menuSong.play(MediaPlayer.INDEFINITE);
        } else {
            menuSong.pause();
        }
    }

    private void addMenuButton(Button button, int index) {
        button.setLayoutX(MENU_BUTTON_START_X);
        button.setLayoutY(MENU_BUTTON_START_Y + index * 70);
        menuButtons.add(button);
        menuPane.getChildren().add(button);
    }

    private void createButton() {
        createStartButton();
        createScoreButton();
        createHelpButton();
        createOptionButton();
        createCreditsButton();
        createExitButton();
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

        optionSubScene = new GameSubScene();
        menuPane.getChildren().add(optionSubScene);
        optionSubScene.getPane().getChildren().add(createOptions());

        scoreSubScene = new GameSubScene();
        menuPane.getChildren().add(scoreSubScene);
    }

//    private void createSoundButton() {
//        ImageView speaker = new ImageView();
//        Image speakerOn = new Image("/model/sound-on.png");
//        Image silent = new Image("/model/silent.png");
//        if (AudioManager.isSoundEnabled()) {
//            speaker.setImage(speakerOn);
//        } else {
//            speaker.setImage(silent);
//        }
//        speaker.setLayoutX(100);
//        speaker.setLayoutY(30);
//
//        speaker.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if (AudioManager.isSoundEnabled()) {
//                    speaker.setImage(silent);
//                    AudioManager.setSoundOn(false);
//                    menuSong.stop();
//                } else {
//                    speaker.setImage(speakerOn);
//                    AudioManager.setSoundOn(true);
//                    menuSong.play(MediaPlayer.INDEFINITE);
//                }
//            }
//        });
//        menuPane.getChildren().add(speaker);
//    }

    private void createStartButton() {
        GameButton startButton = new GameButton("PLAY GAME");
        addMenuButton(startButton, 0);
//        menu = this;

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /*GameViewManager gameView = new GameViewManager(BombermanGame.numStage);
//                gameView.createNewGame();
//                menuStage.close();
                BombermanGame.switchScene(gameView.getScene());*/
                WaitViewManager waitView = new WaitViewManager();
                menuSong.stop();
                BombermanGame.switchScene(waitView.getWaitScene());
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

    private void createOptionButton() {
        GameButton optionButton = new GameButton("OPTION");
        addMenuButton(optionButton, 3);

        optionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(optionSubScene);
            }
        });
    }

    private VBox createOptions() {
        VBox box = new VBox();
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);
        InfoLabel label1 = new InfoLabel("Background Music: ");
        backgroundMusic = new OptionController(AudioManager.BACKGROUND_MUSIC);
        backgroundMusic.changeVolume(menuSong);
        InfoLabel label2 = new InfoLabel("Gameplay Music: ");
        gameplayMusic = new OptionController(AudioManager.GAMEPLAY_MUSIC);
        gameplayMusic.changeVolume(AudioManager.GAMEPLAY_MUSIC);

        box.getChildren().add(label1);
        box.getChildren().add(backgroundMusic);
        box.getChildren().add(label2);
        box.getChildren().add(gameplayMusic);
//        box.setAlignment(Pos.CENTER);
        box.setLayoutX(50);
        box.setLayoutY(50);
        return box;
    }

    private void createCreditsButton() {
        GameButton creditsButton = new GameButton("CREDITS");
        addMenuButton(creditsButton, 4);

        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(creditsSubScene);
            }
        });
    }

    private void createExitButton() {
        GameButton exitButton = new GameButton("EXIT");
        addMenuButton(exitButton, 5);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
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
