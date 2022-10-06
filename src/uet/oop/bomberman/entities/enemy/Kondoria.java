package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemy.PathFinding.AStarAlgorithm;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Kondoria extends Enemy {
    private final static int velocity = 1;

    private final static int SPRITE_WIDTH = Sprite.kondoria_right1.getSpriteHeight();
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
    public void dead() {

    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        kondoriaBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return kondoriaBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof Wall) {
                if (isColliding(entity))
                    return true;
            }
        }
        return false;
    }

    public void choosingSprite() {
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
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
