package uet.oop.bomberman.entities.character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.constants.BombStorage;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.input.KeyManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Bomber extends Character {

//    final static int velocity = 1;
    final static int SPRITE_WIDTH = 24;
    final static int SPRITE_HEIGHT = 28;

    private int velocity = 1;
    private int numBomb = 0;
    private int limitBomb = 0;
    private boolean speedBuff = false;
    private boolean bombBuff = false;
    private boolean flameBuff = false;
    private boolean flagBomb = false;

    KeyManager keyInput;
    Sprite currentSprite;
    RectBoundedBox playerBoundary;

    public Bomber(int x, int y, Image img, KeyManager keyInput) {
        super( x, y, img);
        this.keyInput = keyInput;
        direction = Direction.RIGHT;
        currentSprite = Sprite.player_right;
        playerBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
    }

    @Override
    public void update() {
        move();
        animate();
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        playerBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return playerBoundary;
    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        playerBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return playerBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkCollision() {
        for (Entity entity : GameViewManager.getStillObjects()) {
            if (entity instanceof StaticEntity || entity instanceof Bomb) {
                if(isColliding(entity))
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void move() {
        moving = false;
        if (keyInput.isPressed(KeyCode.UP)) {
            moveUp();
            if (checkCollision()) moveDown();
            direction = Direction.UP;
            moving = true;
        }
        if (keyInput.isPressed(KeyCode.DOWN)) {
            moveDown();
            if (checkCollision()) moveUp();
            direction = Direction.DOWN;
            moving = true;
        }
        if (keyInput.isPressed(KeyCode.LEFT)) {
            moveLeft();
            if (checkCollision()) moveRight();
            direction = Direction.LEFT;
            moving = true;
        }
        if (keyInput.isPressed(KeyCode.RIGHT)) {
            moveRight();
            if (checkCollision()) moveLeft();
            direction = Direction.RIGHT;
            moving = true;
        }

        if (keyInput.isPressed(KeyCode.SPACE) && !flagBomb) {
            createBomb();
            flagBomb = true;
        }
        if (!keyInput.isPressed(KeyCode.SPACE) && flagBomb) {
            flagBomb = false;
        }
    }

    private void createBomb() {
        if (bombBuff) {
            limitBomb = 2;
        }else {
            limitBomb = 100;
        }
        if (numBomb < limitBomb) {
            int xUnit = (this.x + SPRITE_WIDTH / 2) / Sprite.SCALED_SIZE;
            int yUnit = (this.y + SPRITE_HEIGHT / 2) / Sprite.SCALED_SIZE;
            Bomb bomb = new Bomb(xUnit, yUnit, Sprite.bomb_2.getFxImage());
            BombStorage.addBomb(bomb);
//            GameViewManager.getStillObjects().add(bomb);
            numBomb = BombStorage.getNumBomb();
        }
    }

    @Override
    public void kill() {

    }

    @Override
    public void dead() {

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
    public boolean isMoving() {
        return moving;
    }

    private void setCurrentSprite(Sprite sprite) {
        if (sprite != null) {
            currentSprite = sprite;
        } else {
            System.out.println("Missing sprite");
        }
    }

    private void chooseSprite() {
        switch (direction) {
            case UP:
                currentSprite = Sprite.player_up;
                if (isMoving()) {
                    currentSprite = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1,
                                Sprite.player_up_2, animation, 30);
                }
                break;
            case DOWN:
                currentSprite = Sprite.player_down;
                if (isMoving()) {
                    currentSprite = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1,
                                Sprite.player_down_2, animation, 30);
                }
                break;
            case LEFT:
                currentSprite = Sprite.player_left;
                if (isMoving()) {
                    currentSprite = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1,
                                Sprite.player_left_2, animation, 30);
                }
                break;
            case RIGHT:
                currentSprite = Sprite.player_right;
                if (isMoving()) {
                    currentSprite = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1,
                                Sprite.player_right_2, animation, 30);
                }
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        chooseSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }

    public String toString() {
        return "Bomberman{x = " + x + ", y = " + y + ", width = "
                + currentSprite.getSpriteWidth() + ", height = " + currentSprite.getSpriteHeight() + "}";
    }

}
