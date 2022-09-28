package uet.oop.bomberman.entities.character.enemy.PathFinding;

import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.character.Character;

import java.util.Random;

public class RandomMove {
    protected Character enemy;

    public RandomMove(Character enemy) {
        this.enemy = enemy;
    }

    public enum EnemyDirection {
        UP, DOWN, LEFT, RIGHT, DETECT_FAILED
    }

    public void setRandomDirection() {
        if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
            Random random = new Random();
            int dir = random.nextInt(4);
            switch (dir) {
                case 0:
                    enemy.setDirection(Direction.UP);
                    break;
                case 1:
                    enemy.setDirection(Direction.DOWN);
                    break;
                case 2:
                    enemy.setDirection(Direction.LEFT);
                    break;
                case 3:
                    enemy.setDirection(Direction.RIGHT);
                    break;
            }
        }
    }
}
