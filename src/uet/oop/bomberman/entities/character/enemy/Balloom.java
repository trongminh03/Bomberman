package uet.oop.bomberman.entities.character.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

import java.util.Random;

public class Balloom extends Character {
    private final static int velocity = 1;

    private final static int SPRITE_WIDTH = Sprite.balloom_right1.getSpriteWidth();
    private final static int SPRITE_HEIGHT = Sprite.balloom_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox balloomBoundary;

    public Balloom(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        direction = Direction.RIGHT;
        currentSprite = Sprite.balloom_right1;
        moving = true;
        balloomBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    @Override
    public void update() {
        move();
        animate();
    }

    @Override
    protected void move() {
//        System.out.println(toString());
        if (this.x % 32 == 0 && this.y % 32 == 0) {
            Random random = new Random();
            int dir = random.nextInt(4);
            switch (dir) {
                case 0:
                    moveUp();
                    if (checkCollision()) moveDown();
                    direction = Direction.UP;
                    break;
                case 1:
                    moveDown();
                    if (checkCollision()) moveUp();
                    direction = Direction.DOWN;
                    break;
                case 2:
                    moveLeft();
                    if (checkCollision()) moveRight();
                    direction = Direction.LEFT;
                    break;
                case 3:
                    moveRight();
                    if (checkCollision()) moveLeft();
                    direction = Direction.RIGHT;
                    break;
            }
        } else {
            switch (direction) {
                case UP:
                    moveUp();
                    break;
                case DOWN:
                    moveDown();
                    break;
                case LEFT:
                    moveLeft();
                    break;
                case RIGHT:
                    moveRight();
                    break;
            }
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
        balloomBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return balloomBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkCollision() {
        for (Entity entity : GameViewManager.getStillObjects()) {
            if (entity instanceof StaticEntity) {
                if (isColliding(entity))
                    return true;
            }
        }
        return false;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
    }

    public void choosingSprite() {
        switch (direction) {
            case UP:
                currentSprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_right2,
                        Sprite.balloom_left3, animation, 60);
                break;
            case DOWN:
                currentSprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_left2,
                        Sprite.balloom_right3, animation, 60);
                break;
            case LEFT:
                currentSprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2,
                        Sprite.balloom_left3, animation, 60);
                break;
            case RIGHT:
                currentSprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2,
                        Sprite.balloom_right3, animation, 60);
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }

    @Override
    public String toString() {
        return "Balloom{x = " + x + ", y = " + y + "}";
    }
}
