package uet.oop.bomberman.entities.character.enemy.PathFinding;

import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.gui.GameViewManager;

import java.util.List;
import java.util.Random;

public class PathFindingLv1 extends RandomMove {
    private Bomber bomber;
//    private Character enemy;
    private GameViewManager game;

    public enum EnemyDirection {
        UP, DOWN, LEFT, RIGHT, DETECT_FAILED
    }

    public PathFindingLv1(Character enemy, Bomber bomber, GameViewManager game) {
        super(enemy);
        this.bomber = bomber;
        this.game = game;
    }

    private EnemyDirection chasePlayer() {
        if (bomber.getGridY() == enemy.getGridY() && bomber.getGridX() - enemy.getGridX() <= 5
                        && bomber.getGridX() - enemy.getGridX() > 0) {
            for (Entity object : game.getStillObjects()) {
                if (object instanceof StaticEntity) {
                    if (enemy.getGridY() == object.getGridY() && object.getGridX() > enemy.getGridX()
                            && bomber.getGridX() > object.getGridX()) {
                        return EnemyDirection.DETECT_FAILED;
                    }
                }
            }
            return EnemyDirection.RIGHT;
        }

        if (bomber.getGridY() == enemy.getGridY() && enemy.getGridX() - bomber.getGridX() <= 5
                && enemy.getGridX() - bomber.getGridX() > 0) {
            for (Entity object : game.getStillObjects()) {
                if (object instanceof StaticEntity) {
                    if (enemy.getGridY() == object.getGridY() && enemy.getGridX() > object.getGridX()
                            && object.getGridX() > bomber.getGridX()) {
                        return EnemyDirection.DETECT_FAILED;
                    }
                }
            }
            return EnemyDirection.LEFT;
        }

        if (bomber.getGridX() == enemy.getGridX() && enemy.getGridY() - bomber.getGridY() <= 5
                && enemy.getGridY() - bomber.getGridY() > 0) {
            for (Entity object : game.getStillObjects()) {
                if (object instanceof StaticEntity) {
                    if (enemy.getGridX() == object.getGridX() && object.getGridY() > bomber.getGridY()
                            && object.getGridY() < enemy.getGridY()) {
                        return EnemyDirection.DETECT_FAILED;
                    }
                }
            }
            return EnemyDirection.UP;
        }

        if (bomber.getGridX() == enemy.getGridX() && bomber.getGridY() - enemy.getGridY() <= 5
                && bomber.getGridY() - enemy.getGridY() > 0) {
            for (Entity object : game.getStillObjects()) {
                if (object instanceof StaticEntity) {
                    if (enemy.getGridX() == object.getGridX() && bomber.getGridY() > object.getGridY()
                            && enemy.getGridY() < object.getGridY()) {
                        return EnemyDirection.DETECT_FAILED;
                    }
                }
            }
            return EnemyDirection.DOWN;
        }

        return EnemyDirection.DETECT_FAILED;
    }

    public void updateEnemyDirection() {
        EnemyDirection dir = chasePlayer();
        if (dir == EnemyDirection.RIGHT) {
            enemy.setDirection(Direction.RIGHT);
        } else if (dir == EnemyDirection.LEFT) {
            enemy.setDirection(Direction.LEFT);
        } else if (dir == EnemyDirection.UP) {
            enemy.setDirection(Direction.UP);
        } else if (dir == EnemyDirection.DOWN) {
            enemy.setDirection(Direction.DOWN);
        } else {
            setRandomDirection();
        }
    }
}
