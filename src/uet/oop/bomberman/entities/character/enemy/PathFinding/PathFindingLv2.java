package uet.oop.bomberman.entities.character.enemy.PathFinding;

import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.gui.GameViewManager;

public class PathFindingLv2 extends RandomMove {
    private Bomber bomber;
    private GameViewManager game;
    private static final int SCOPE = 5;
    private boolean startX = false;
    private boolean startY = false;
    private boolean endX = false;
    private boolean endY = false;

    public PathFindingLv2(Character enemy, Bomber bomber, GameViewManager game) {
        super(enemy);
        this.game = game;
        this.bomber = game.getBomberman();
    }

    public EnemyDirection chasePlayer() {
        // reset path finding
        startX = false;
        startY = false;
        endX = false;
        endY = false;

        // check exact grid
        if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
            if (Math.abs(bomber.getGridX() - enemy.getGridX()) <= SCOPE
                    && Math.abs(bomber.getGridY() - enemy.getGridY()) <= SCOPE
                    && Math.abs(bomber.getGridX() - enemy.getGridX()) >= 0
                    && Math.abs(enemy.getGridY() - bomber.getGridY()) >= 0) { // check if bomber in scope
                // set all path is true
                startX = true;
                startY = true;
                endX = true;
                endY = true;
                for (Entity object : game.getStillObjects()) {
                    if (object instanceof StaticEntity) {
                        if (Math.abs(bomber.getGridX() - object.getGridX()) + Math.abs(object.getGridX() - enemy.getGridX())
                                == Math.abs(bomber.getGridX() - enemy.getGridX())) { // if objects in X axis
                            if (enemy.getGridY() == object.getGridY()) { // if objects block startX
                                startX = false;
                            }
                            if (bomber.getGridY() == object.getGridY()) { // if objects block endX
                                endX = false;
                            }
                        }
                        if (Math.abs(bomber.getGridY() - object.getGridY()) + Math.abs(object.getGridY() - enemy.getGridY())
                                == Math.abs(bomber.getGridY() - enemy.getGridY())) { // if objects in Y axis
                            if (enemy.getGridX() == object.getGridX()) { // if objects block startY
                                startY = false;
                            }
                            if (bomber.getGridX() == object.getGridX()) { // if objects block endY
                                endY = false;
                            }
                        }
                    }
                }
            }
        }

        if (startX && endY) {
            if (bomber.getGridX() > enemy.getGridX()) {
                return EnemyDirection.RIGHT;
            } else if (bomber.getGridX() < enemy.getGridX()) {
                return EnemyDirection.LEFT;
            } else {
                if (bomber.getGridY() > enemy.getGridY()) {
                    return EnemyDirection.DOWN;
                } else {
                    return EnemyDirection.UP;
                }
            }
        } else if (startY && endX) {
            if (bomber.getGridY() > enemy.getGridY()) {
                return EnemyDirection.DOWN;
            } else if (bomber.getGridY() < enemy.getGridY()) {
                return EnemyDirection.UP;
            } else {
                if (bomber.getGridX() > enemy.getGridX()) {
                    return EnemyDirection.RIGHT;
                } else {
                    return EnemyDirection.LEFT;
                }
            }
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
