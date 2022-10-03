package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.enemy.PathFinding.PathFindingLv2;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

import javax.print.attribute.standard.Finishings;

public class Minvo extends Enemy {
    private final static int velocity = 1;

    private final static int SPRITE_WIDTH = Sprite.minvo_right1.getSpriteHeight();
    private final static int SPRITE_HEIGHT = Sprite.minvo_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox minvoBoundary;
    private GameViewManager game;

    PathFindingLv2 pathFinding;

    public Minvo(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        direction = Direction.RIGHT;
        currentSprite = Sprite.minvo_right1;
        moving = true;
        minvoBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.game = game;
        pathFinding = new PathFindingLv2(this, game.getBomberman(), game);
        FINDING_SCOPE = 5;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        minvoBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return minvoBoundary;
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
    public void kill() {

    }

    @Override
    public void dead() {

    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        minvoBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return minvoBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof StaticEntity) {
                if (isColliding(entity))
                    return true;
            }
        }
        return false;
    }

    public void choosingSprite() {
        switch (direction) {
            case UP:
                currentSprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_right2,
                        Sprite.minvo_left3, animation, 60);
                break;
            case DOWN:
                currentSprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_left2,
                        Sprite.minvo_right3, animation, 60);
                break;
            case LEFT:
                currentSprite = Sprite.movingSprite(Sprite.minvo_left1, Sprite.minvo_left2,
                        Sprite.minvo_left3, animation, 60);
                break;
            case RIGHT:
                currentSprite = Sprite.movingSprite(Sprite.minvo_right1, Sprite.minvo_right2,
                        Sprite.minvo_right3, animation, 60);
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
