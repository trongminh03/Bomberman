package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemy.PathFinding.AStarAlgorithm;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Kondoria extends Enemy {
    private int velocity;
    private final static int SPRITE_WIDTH = Sprite.kondoria_right1.getSpriteWidth();
    private final static int SPRITE_HEIGHT = Sprite.kondoria_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox kondoriaBoundary;
    private GameViewManager game;

    AStarAlgorithm pathFinding;

    public Kondoria(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        direction = Direction.RIGHT;
        brickPass = true;
        currentSprite = Sprite.kondoria_right1;
        FINDING_SCOPE = 7;
        moving = true;
        velocity = 1;
        kondoriaBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.game = game;
        pathFinding = new AStarAlgorithm(this, game.getBomberman(), game);
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        kondoriaBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return kondoriaBoundary;
    }

    @Override
    public void update() {
        move();
        animate();
    }

    @Override
    protected void move() {
        pathFinding.updateEnemyDirection();
        switch (direction) {
            case UP:
                moveUp();
                if (checkSafeCollision()) moveDown();
                break;
            case DOWN:
                moveDown();
                if (checkSafeCollision()) moveUp();
                break;
            case RIGHT:
                moveRight();
                if (checkSafeCollision()) moveLeft();
                break;
            case LEFT:
                moveLeft();
                if (checkSafeCollision()) moveRight();
                break;
        }
    }

    public void moveUp() {
        y -= velocity;
    }

    public void moveDown() {
        y += velocity;
    }

    public void moveRight() {
        x += velocity;
    }

    public void moveLeft() {
        x -= velocity;
    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        kondoriaBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return kondoriaBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
//        for (Entity entity : game.getStillObjects()) {
//            if (entity instanceof Wall) {
//                if (isColliding(entity))
//                    return true;
//            }
//        }
//
//        for (Bomb bomb : game.getBomberman().getBombs()) {
//            if (bomb.getBombStatus() != BombStatus.DESTROY) {
//                if (isColliding(bomb) && !bomb.isThroughBomb()) {
//                    return true;
//                }
//            }
//        }
//        return false;
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof Wall || entity instanceof Bomb) {
                if (entity instanceof Bomb) {
                    Bomb bomb = (Bomb) entity;
                    if (bomb.getBombStatus() != BombStatus.DESTROY) {
                        if (isColliding(bomb) && !bomb.isThroughBomb()) {
                            return true;
                        }
                    }
                } else {
                    if (isColliding(entity))
                        return true;
                }
            }
        }
        return false;
    }

    public void choosingSprite() {
        if (isAlive()) {
            switch (direction) {
                case UP:
                    currentSprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_right2,
                            Sprite.kondoria_left3, animation, 60);
                    break;
                case DOWN:
                    currentSprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_left2,
                            Sprite.kondoria_right3, animation, 60);
                    break;
                case LEFT:
                    currentSprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2,
                            Sprite.kondoria_left3, animation, 60);
                    break;
                case RIGHT:
                    currentSprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2,
                            Sprite.kondoria_right3, animation, 60);
                    break;
            }
        }else {
            if (!resetAnimation) {
                animation = 0;
                resetAnimation = true;
            }
            velocity = 0;
            currentSprite = Sprite.movingSprite(Sprite.kondoria_dead, Sprite.mob_dead1, Sprite.mob_dead2,
                    Sprite.mob_dead3, animation, 40);
            time += elapsedTime;
            if (time == 35 * elapsedTime) {
                game.getEnemieGarbage().add(this);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
