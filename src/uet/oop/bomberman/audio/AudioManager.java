package uet.oop.bomberman.audio;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.io.File;

public class AudioManager {
    public static final String BACKGROUND_MUSIC = "BACKGROUND MUSIC";
    public static final String GAMEPLAY_MUSIC = "GAMEPLAY MUSIC";

    public static double BGVolume = 0.5;
    public static double GPVolume = 0.25;

    private Media media;
    private MediaPlayer mediaPlayer;
    private static boolean BGSoundOn = true;
    private static boolean GPSoundOn = true;
    private String type;

    public AudioManager() {

    }

    public AudioManager(String filePath, String type) {
        media = new Media(new File(filePath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        this.type = type;
    }

    public void play(int loop) {
        if (this.type.equals(BACKGROUND_MUSIC)) {
            mediaPlayer.setVolume(BGVolume);
            mediaPlayer.setCycleCount(loop);
            mediaPlayer.play();
        } else {
            mediaPlayer.setVolume(GPVolume);
            mediaPlayer.setCycleCount(loop);
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        }
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public static boolean isSoundEnabled(String type) {
        if (type.equals(BACKGROUND_MUSIC)) {
            return BGSoundOn;
        } else {
            return GPSoundOn;
        }
    }

    public static void setSoundOn(boolean soundOn, String type) {
        if (type.equals(BACKGROUND_MUSIC)) {
            BGSoundOn = soundOn;
        } else {
            GPSoundOn = soundOn;
        }
    }

    public static void setVolume(double volume, String type) {
        if (type.equals(BACKGROUND_MUSIC)) {
            BGVolume = volume;
        } else {
            GPVolume = volume;
        }
    }

    public String getType() {
        return type;
    }
}
