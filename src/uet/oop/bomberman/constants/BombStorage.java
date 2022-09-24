package uet.oop.bomberman.constants;

import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

public class BombStorage {
    public static LinkedList<Bomb> bombQueue = new LinkedList<>();
    private static Iterator bombIterator = bombQueue.iterator();

    public static LinkedList getBombList() {
        return bombQueue;
    }

    public static void addBomb(Bomb bomb) {
        bombQueue.offer(bomb);
    }

    public static void removeBomb(Bomb bomb) {
        /*while (bombIterator.hasNext()) {
            if (bombIterator.next() == bomb) {
                bombIterator.remove();
                break;
            }
        }*/
        bombQueue.remove();
    }

    public static int getNumBomb() {
        return bombQueue.size();
    }
}
