package uet.oop.bomberman.info;

public class Score {
    public static int score = 0;

    public static void addScore(int score) {
        Score.score += score;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Score.score = score;
    }

    public static void resetScore() {
        score = 0;
    }

    @Override
    public String toString() {
        return Integer.toString(score);
    }
}
