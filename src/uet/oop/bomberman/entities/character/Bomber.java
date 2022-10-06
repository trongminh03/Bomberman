package uet.oop.bomberman.entities.character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.static_objects.StaticEntity;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.input.KeyManager;
import uet.oop.bomberman.model.RectBoundedBox;

import java.util.Timer;
import java.util.TimerTask;

public class Bomber extends Character {
    private int velocity = 2;
    private final static int SPRITE_WIDTH = 24;
    final static int SPRITE_HEIGHT = 32;
//    private Point2D step;
//    private int step;

    KeyManager keyInput;
    private Sprite currentSprite;
    private RectBoundedBox playerBoundary;
    private boolean hitEnemy = false;
    private boolean resetAnimation = false;
    private GameViewManager game;

    public Bomber(int x, int y, Image img, KeyManager keyInput, GameViewManager game) {
        super(x, y, img);
        this.keyInput = keyInput;
        this.game = game;
        direction = Direction.RIGHT;
//        moving = true;
        currentSprite = Sprite.player_right;
        playerBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
    }


    @Override
    public void update() {
//        if (isMoving()) {
//            move();
//        }
        move();
        animate();
        if (checkFatalCollision()) {
            dead();
        }
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

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof Wall || entity instanceof Brick) {
                if (isColliding(entity)) {
//                    System.out.println("Collide");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkFatalCollision() {
        for (Entity entity : game.getEnemies()) {
            if (isColliding(entity)) {
                hitEnemy = true;
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
            if (checkSafeCollision()) moveDown();
            direction = Direction.UP;
            moving = true;
        }
        if (keyInput.isPressed(KeyCode.DOWN)) {
            moveDown();
            if (checkSafeCollision()) moveUp();
            direction = Direction.DOWN;
            moving = true;
        }
        if (keyInput.isPressed(KeyCode.LEFT)) {
            moveLeft();
            if (checkSafeCollision()) moveRight();
            direction = Direction.LEFT;
            moving = true;
        }
        if (keyInput.isPressed(KeyCode.RIGHT)) {
            moveRight();
            if (checkSafeCollision()) moveLeft();
            direction = Direction.RIGHT;
            moving = true;
        }
    }

    @Override
    public void kill() {

    }

    @Override
    public void dead() {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                alive = false;
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1700);
    }

    public void moveUp() {
        y -= velocity;
    }

    public void moveDown() {
        y += velocity;
    }

    public void moveLeft() {
        x -= velocity;
    }

    public void moveRight() {
        x += velocity;
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

//    private void setCurrentSprite(Sprite sprite) {
//        if (sprite != null) {
//            currentSprite = sprite;
//        } else {
//            System.out.println("Missing sprite");
//        }
//    }

    private void chooseSprite() {
        if (!hitEnemy) {
            switch (direction) {
                case UP:
                    currentSprite = Sprite.player_up;
                    if (isMoving()) {
//                    System.out.println("UP");
                        currentSprite = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1,
                                Sprite.player_up_2, animation, 15);
                    }
                    break;
                case DOWN:
                    currentSprite = Sprite.player_down;
                    if (isMoving()) {
//                    System.out.println("DOWN");
                        currentSprite = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1,
                                Sprite.player_down_2, animation, 15);
                    }
                    break;
                case LEFT:
                    currentSprite = Sprite.player_left;
                    if (isMoving()) {
//                    System.out.println("LEFT");
                        currentSprite = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1,
                                Sprite.player_left_2, animation, 15);
                    }
                    break;
                case RIGHT:
                    currentSprite = Sprite.player_right;
                    if (isMoving()) {
//                    System.out.println("RIGHT");
                        currentSprite = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1,
                                Sprite.player_right_2, animation, 15);
                    }
                    break;
            }
        } else {
            if (!resetAnimation) {
                animation = 0;
                resetAnimation = true;
            }
            velocity = 0;
//            moving = false;
            currentSprite = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2,
                    Sprite.player_dead3, animation, 60);
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

    public void setPosition(int xUnit, int yUnit) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
    }

    public boolean checkHitEnemy() {
        return hitEnemy;
    }
}
