package uet.oop.bomberman.audio;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.io.File;

public class AudioManager {
    private Media media;
    private MediaPlayer mediaPlayer;
    private static boolean soundEnabled = true;

    public AudioManager() {

    }

    public AudioManager(String filePath) {
        media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    public void play(int loop) {
        mediaPlayer.setCycleCount(loop);
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static void setSoundOn(boolean soundOn) {
        AudioManager.soundEnabled = soundOn;
    }
}
