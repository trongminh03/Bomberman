package uet.oop.bomberman.constants;

public class GlobalConstants {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 15;
    public static final String GAME_NAME = "Bomberman";
    public static final String GAME_VERSION = " v1.5";
    public static enum GameStatus {
        RUNNING,
        PAUSED,
        GAME_OVER
    }
}
