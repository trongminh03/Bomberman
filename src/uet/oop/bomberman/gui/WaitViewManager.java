package uet.oop.bomberman.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.info.Score;
import uet.oop.bomberman.input.KeyManager;

import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WaitViewManager {
    private static AudioManager nextlevelAudio =
            new AudioManager("res/audio/next_level.mp3", AudioManager.BACKGROUND_MUSIC);
    private static AudioManager gameOverAudio =
            new AudioManager("res/audio/game_over.mp3", AudioManager.BACKGROUND_MUSIC);
    private static AudioManager winGameAudio =
            new AudioManager("res/audio/win_game.mp3", AudioManager.BACKGROUND_MUSIC);
    private final static String fontPath = "res/model/font/PixelEmulator-xq08.ttf";
    private static KeyManager key;
    private AnchorPane pane;
    private static Label[] messages; //messages[0] : Ten man hinh: Stage + num, Game Over, You Win
    //messages[1] : "Your score: " + score
    //messages[2] : "Press any keyboard to back menu
    private static Scene scene;
    private Stage stage;

    public WaitViewManager() {
        key = new KeyManager();
        pane = new AnchorPane();
        scene = new Scene(pane, Sprite.SCALED_SIZE * GameViewManager.WIDTH,
                Sprite.SCALED_SIZE * GameViewManager.HEIGHT);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        messages = new Label[3];
        for (int i = 0; i < 3; i++) {
            messages[i] = new Label();
            messages[i].setTextFill(Color.WHITE);
            pane.getChildren().add(messages[i]);
        }
        try {
            messages[0].setFont(Font.loadFont(new FileInputStream(fontPath), 43));
        } catch (FileNotFoundException e) {
            messages[0].setFont(new Font("Verdana", 50));
        }
        stage = new Stage();
        stage.setScene(scene);
    }

    public static Scene getWinGameScene() {
        messages[0].setText("You Win !!!");
        messages[0].setLayoutX(195);
        messages[0].setLayoutY(200);
        messages[1].setText("Your score:   " + Score.getScore());
        messages[1].setLayoutX(230);
        messages[1].setLayoutY(250);
        try {
            messages[1].setFont(Font.loadFont(new FileInputStream(fontPath), 19));
        } catch (FileNotFoundException e) {
            messages[1].setFont(new Font("Verdana", 26));
        }

        if (AudioManager.isSoundEnabled(AudioManager.BACKGROUND_MUSIC)) {
            winGameAudio.play(1);
        }
        new Timeline(new KeyFrame(
                Duration.millis(8000),
                event -> {
                    BombermanGame.switchScene(MenuViewManager.getScene());
                    Score.resetScore();
                }
        )).play();
        return scene;
    }

    public static Scene getGamePlayScene() {
        messages[0].setText("Stage  " + BombermanGame.numStage);
        messages[0].setLayoutX(195);
        messages[0].setLayoutY(200);
        if (AudioManager.isSoundEnabled(AudioManager.BACKGROUND_MUSIC)) {
            nextlevelAudio.play(1);
        }
        navigateGame();
        return scene;
    }

    public static Scene getGameOverScene() {
        gameOverAudio.play(-1);
        messages[0].setText("Game Over");
        messages[0].setLayoutX(185);
        messages[0].setLayoutY(170);
        messages[1].setText("Your score:   " + Score.getScore());
        messages[1].setLayoutX(230);
        messages[1].setLayoutY(250);
        try {
            messages[1].setFont(Font.loadFont(new FileInputStream(fontPath), 19));
        } catch (FileNotFoundException e) {
            messages[1].setFont(new Font("Verdana", 26));
        }
        messages[2].setText("Press any key to return main menu");
        messages[2].setLayoutX(160);
        messages[2].setLayoutY(450);
        try {
            messages[2].setFont(Font.loadFont(new FileInputStream(fontPath), 14));
        } catch (FileNotFoundException e) {
            messages[2].setFont(new Font("Verdana", 19));
        }
        scene.setOnKeyPressed(keyEvent -> {
            gameOverAudio.stop();
            BombermanGame.switchScene(MenuViewManager.getScene());
            Score.resetScore();
            MenuViewManager.playMenuMusic();
        });
        return scene;
    }

    public static void navigateGame() {
        new Timeline(new KeyFrame(
                Duration.millis(3000),
                event -> {
                    GameViewManager gameView = new GameViewManager(BombermanGame.numStage);
                    BombermanGame.switchScene(gameView.getScene());
                }
        )).play();
    }

    public static void reset() {

    }
}
