package uet.oop.bomberman.constants;

import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.static_objects.Brick;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

public class Storage {
    public static Vector<Bomb> bombVector = new Vector<>();
    public static Vector<Bomb> bombGarbage = new Vector<>();

    public static Vector<Brick> brickVector = new Vector<>();
    public static Vector<Brick> brickGarbage = new Vector<>();

    public static Vector<Bomb> getBombVector() {
        return bombVector;
    }

    public static Vector<Brick> getBickVector() {
        return brickVector;
    }

    public static void addBomb(Bomb bomb) {
        bombVector.add(bomb);
    }

    public static void addBrick(Brick brick) {
        brickVector.add(brick);
    }

    public static void addBombGarbage(Bomb bomb) {
        bombGarbage.add(bomb);
    }

    public static void addBrickGarbage(Brick brick) {
        brickGarbage.add(brick);
    }

    public static void clearGarbage() {
        brickVector.removeAll(brickGarbage);
        bombVector.removeAll(bombGarbage);
    }

    public static int getNumBomb() {
        return bombVector.size();
    }
}
