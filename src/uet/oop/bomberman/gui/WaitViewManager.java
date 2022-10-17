package uet.oop.bomberman.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WaitViewManager {
    private AudioManager nextlevelAudio =
            new AudioManager("res/audio/next_level.mp3", AudioManager.BACKGROUND_MUSIC);
    private final static String fontPath = "res/model/font/PixelEmulator-xq08.ttf";
    private StackPane pane;
    private Label message;
    private Scene scene;
    private Stage stage;

    public WaitViewManager() {
        pane = new StackPane();
        scene = new Scene(pane, Sprite.SCALED_SIZE * GameViewManager.WIDTH,
                Sprite.SCALED_SIZE * GameViewManager.HEIGHT);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        message = new Label("Stage  " + BombermanGame.numStage);
        try {
            message.setFont(Font.loadFont(new FileInputStream(fontPath), 43));
        } catch (FileNotFoundException e) {
            message.setFont(new Font("Verdana", 50));
        }
        message.setTextFill(Color.WHITE);
        pane.getChildren().add(message);
        stage = new Stage();
        stage.setScene(scene);
    }

    public Scene getWaitScene() {
        if (AudioManager.isSoundEnabled(AudioManager.BACKGROUND_MUSIC)) {
            nextlevelAudio.play(1);
        }
        navigateGame();
        return scene;
    }

    public void navigateGame() {
        new Timeline(new KeyFrame(
                Duration.millis(3000),
                event -> {
                    GameViewManager gameView = new GameViewManager(BombermanGame.numStage);
                    BombermanGame.switchScene(gameView.getScene());
                }
        )).play();
    }
}
