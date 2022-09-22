package uet.oop.bomberman.constants;

import uet.oop.bomberman.entities.Bomb;

import java.util.ArrayList;
import java.util.List;

public class BombStorage {

    public static List<Bomb> bombList = new ArrayList<>();

    public static List<Bomb> getBombList() {
        return bombList;
    }

    public static void addBomb(Bomb bomb) {
        bombList.add(bomb);
    }

    public static Bomb getBomb(int index) {
        return bombList.get(index);
    }

    public static void removeBomb(int index) {
        bombList.remove(index);
    }

    public static void removeBomb(Bomb bomb) {
        bombList.remove(bomb);
    }

    public static int getNumOfBomb() {
        return bombList.size();
    }
}
