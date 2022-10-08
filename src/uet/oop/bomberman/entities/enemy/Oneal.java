package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.static_objects.StaticEntity;
import uet.oop.bomberman.entities.enemy.PathFinding.PathFindingLv1;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

import java.util.Random;

public class Oneal extends Enemy {
    private int velocity;

    private final static int SPRITE_WIDTH = Sprite.oneal_right1.getSpriteWidth();
    private final static int SPRITE_HEIGHT = Sprite.oneal_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox onealBoundary;
    private GameViewManager game;

    private final PathFindingLv1 pathFinding;

    public Oneal(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        direction = Direction.RIGHT;
        currentSprite = Sprite.balloom_right1;
        moving = true;
        onealBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.game = game;
        pathFinding = new PathFindingLv1(this, game.getBomberman(), game);
        velocity = 1;
        FINDING_SCOPE = 5;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        onealBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return onealBoundary;
    }

    @Override
    public void update() {
        move();
        animate();
    }

    @Override
    protected void move() {
//        System.out.println(toString());
        pathFinding.updateEnemyDirection();
        if (velocity != 0) {
            Random random = new Random();
            velocity = random.nextInt(2) + 1; // velocity random [1, 2]
        }
//        System.out.println(velocity);
//        System.out.println(direction.toString());
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
        onealBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return onealBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof Wall || entity instanceof Brick || entity instanceof Bomb) {
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
                    currentSprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_right2,
                            Sprite.oneal_left3, animation, 60);
                    break;
                case DOWN:
                    currentSprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_left2,
                            Sprite.oneal_right3, animation, 60);
                    break;
                case LEFT:
                    currentSprite = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2,
                            Sprite.oneal_left3, animation, 60);
                    break;
                case RIGHT:
                    currentSprite = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2,
                            Sprite.oneal_right3, animation, 60);
                    break;
            }
        }else {
            if (!resetAnimation) {
                animation = 0;
                resetAnimation = true;
            }
            velocity = 0;
            currentSprite = Sprite.movingSprite(Sprite.oneal_dead, Sprite.mob_dead1, Sprite.mob_dead2,
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
