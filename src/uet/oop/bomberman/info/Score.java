package uet.oop.bomberman.info;

import java.io.*;
import java.util.*;

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
        scoreList.clear();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                scoreList.add(Integer.parseInt(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateTopScore() {
        if (score > scoreList.get(2)) {
            scoreList.add(score);
            scoreList.sort(Collections.reverseOrder());
            scoreList.remove(3);
            recordScore();
        }
    }

    public static void recordScore() {
        File file = new File(path);
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            for (int score : scoreList) {
                bf.write(Integer.toString(score));
                bf.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return Integer.toString(score);
    }
}
