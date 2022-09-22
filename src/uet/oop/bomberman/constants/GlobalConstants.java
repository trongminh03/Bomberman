package uet.oop.bomberman.constants;

public class GlobalConstants {
    public static final int WIDTH = 25;
    public static final int HEIGHT = 15;
    public static final String GAME_NAME = "Bomberman";
    public static final String GAME_VERSION = " v1.0";
    public static enum GameStatus {
        RUNNING,
        PAUSED,
        GAME_OVER
    }
}
