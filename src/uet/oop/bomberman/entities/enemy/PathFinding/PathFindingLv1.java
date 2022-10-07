package uet.oop.bomberman.entities.enemy.PathFinding;

import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.entities.static_objects.Brick;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.gui.GameViewManager;

public class PathFindingLv1 extends RandomMove {
    private Bomber bomber;
    private GameViewManager game;

    public PathFindingLv1(Enemy enemy, Bomber bomber, GameViewManager game) {
        super(enemy);
        this.bomber = bomber;
        this.game = game;
    }

    private EnemyDirection chasePlayer() {
        // bomber is on the right side of enemy
        if (bomber.getGridY() == enemy.getGridY() && bomber.getGridX() - enemy.getGridX() <= enemy.getFindingScope()
                && bomber.getGridX() - enemy.getGridX() > 0) {
            // check exact grid
            if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
                for (Entity object : game.getStillObjects()) {
                    if (object instanceof Wall || object instanceof Brick || object instanceof Bomb) {
                        if (enemy.getGridY() == object.getGridY() && object.getGridX() > enemy.getGridX()
                                && bomber.getGridX() > object.getGridX()) {
                            return EnemyDirection.DETECT_FAILED;
                        }
                    }
                }
                return EnemyDirection.RIGHT;
            }
        }

        // bomber is on the left side of enemy
        if (bomber.getGridY() == enemy.getGridY() && enemy.getGridX() - bomber.getGridX() <= enemy.getFindingScope()
                && enemy.getGridX() - bomber.getGridX() > 0) {
            // check exact grid
            if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
                for (Entity object : game.getStillObjects()) {
                    if (object instanceof Wall || object instanceof Brick || object instanceof Bomb) {
                        if (enemy.getGridY() == object.getGridY() && enemy.getGridX() > object.getGridX()
                                && object.getGridX() > bomber.getGridX()) {
                            return EnemyDirection.DETECT_FAILED;
                        }
                    }
                }
                return EnemyDirection.LEFT;
            }
        }

        // bomber is higher enemy
        if (bomber.getGridX() == enemy.getGridX() && enemy.getGridY() - bomber.getGridY() <= enemy.getFindingScope()
                && enemy.getGridY() - bomber.getGridY() > 0) {
            // check exact grid
            if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
                for (Entity object : game.getStillObjects()) {
                    if (object instanceof Wall || object instanceof Brick || object instanceof Bomb) {
                        if (enemy.getGridX() == object.getGridX() && object.getGridY() > bomber.getGridY()
                                && object.getGridY() < enemy.getGridY()) {
                            return EnemyDirection.DETECT_FAILED;
                        }
                    }
                }
                return EnemyDirection.UP;
            }
        }

        // bomber is lower enemy
        if (bomber.getGridX() == enemy.getGridX() && bomber.getGridY() - enemy.getGridY() <= enemy.getFindingScope()
                && bomber.getGridY() - enemy.getGridY() > 0) {
            // check exact grid
            if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
                for (Entity object : game.getStillObjects()) {
                    if (object instanceof Wall || object instanceof Brick || object instanceof Bomb) {
                        if (enemy.getGridX() == object.getGridX() && bomber.getGridY() > object.getGridY()
                                && enemy.getGridY() < object.getGridY()) {
                            return EnemyDirection.DETECT_FAILED;
                        }
                    }
                }
                return EnemyDirection.DOWN;
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
