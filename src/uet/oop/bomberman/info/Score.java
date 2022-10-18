package uet.oop.bomberman.info;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Score {
    public static int score = 0;
    public static List<Integer> scoreList = new ArrayList<>();
    private final static String path = "res/levels/ScoreList.txt";


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

    public static void readScoreListFile() {
        File file = new File(path);
        try {
            Scanner sc = new Scanner(file);
            scoreList.clear();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                scoreList.add(Integer.parseInt(line));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return Integer.toString(score);
    }
}
