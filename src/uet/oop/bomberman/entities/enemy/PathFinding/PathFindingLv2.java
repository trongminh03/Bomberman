package uet.oop.bomberman.entities.enemy.PathFinding;

import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;

public class PathFindingLv2 extends RandomMove {
    private Bomber bomber;
    private GameViewManager game;
    private boolean startX = false;
    private boolean startY = false;
    private boolean endX = false;
    private boolean endY = false;

    public PathFindingLv2(Enemy enemy, Bomber bomber, GameViewManager game) {
        super(enemy);
        this.game = game;
        this.bomber = bomber;
    }

    public EnemyDirection chasePlayer() {
        // reset path finding
        startX = false;
        startY = false;
        endX = false;
        endY = false;

        if (Math.abs(bomber.getGridX() - enemy.getGridX()) <= enemy.getFindingScope()
                && Math.abs(bomber.getGridY() - enemy.getGridY()) <= enemy.getFindingScope()
                && Math.abs(bomber.getGridX() - enemy.getGridX()) >= 0
                && Math.abs(enemy.getGridY() - bomber.getGridY()) >= 0) { // check if bomber in enemy.getFindingScope()
            if (enemy.getX() % Sprite.SCALED_SIZE == 0 && enemy.getY() % Sprite.SCALED_SIZE == 0) { // check exact grid
                // set all path is true
                startX = true;
                startY = true;
                endX = true;
                endY = true;
                for (Entity object : game.getStillObjects()) {
                    if (object instanceof Wall || object instanceof Brick) {
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

    public EnemyDirection chasePlayerBrickPass() {
        // reset path finding
        startX = false;
        startY = false;
        endX = false;
        endY = false;

        if (Math.abs(bomber.getGridX() - enemy.getGridX()) <= enemy.getFindingScope()
                && Math.abs(bomber.getGridY() - enemy.getGridY()) <= enemy.getFindingScope()
                && Math.abs(bomber.getGridX() - enemy.getGridX()) >= 0
                && Math.abs(enemy.getGridY() - bomber.getGridY()) >= 0) { // check if bomber in enemy scope
            // check exact grid
            if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
                // set all path is true
                startX = true;
                startY = true;
                endX = true;
                endY = true;
                for (Entity object : game.getStillObjects()) {
                    if (object instanceof Wall) {
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

    public void updateEnemyDirectionBrickPass() {
        EnemyDirection dir = chasePlayerBrickPass();
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
