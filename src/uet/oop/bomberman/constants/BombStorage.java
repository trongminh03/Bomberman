package uet.oop.bomberman.constants;

import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

public class BombStorage {
    public static Vector<Bomb> bombVector = new Vector<>();
    public static Vector<Bomb> bombGarbage = new Vector<>();
    public static Vector<Bomb> getBombVector() {
        return bombVector;
    }

    public static void addBomb(Bomb bomb) {
        bombVector.add(bomb);
    }

    public static void addBombGarbage(Bomb bomb) {
        bombGarbage.add(bomb);
    }

    public static void clearGarbage() {
        bombVector.removeAll(bombGarbage);
    }

    public static int getNumBomb() {
        return bombVector.size();
    }
}
