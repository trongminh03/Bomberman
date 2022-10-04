package uet.oop.bomberman.entities.character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.constants.Storage;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.static_objects.Brick;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.input.KeyManager;
import uet.oop.bomberman.model.RectBoundedBox;

import java.util.Timer;
import java.util.TimerTask;

public class Bomber extends Character {
    private static final int velocity = 2;

    final static int BOMBER_WIDTH = 24;
    final static int BOMBER_HEIGHT = 28;

    private int numBomb = 0;
    private int limitBomb = 0;
    private Bomb[] bombs = new Bomb[5];

    private boolean isSpeedBuff = false;
    private boolean isBombBuff = false;
    private boolean isFlameBuff = false;
    private boolean isPlacedBomb = false;   /* Check place bomb: true: bomber placed bomb, can't place bombs after a short amount of time
                                                                 false: bomber no bomb yet, can place bombs now */

    private final double elapsedTime = 1/30f;
    private double time = 10;

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
        currentSprite = Sprite.player_right;
        playerBoundary = new RectBoundedBox(x, y, BOMBER_WIDTH, BOMBER_HEIGHT);
    }

    @Override
    public void update() {
        move();
        animate();
        if (checkFatalCollision()) {
            dead();
        }
        if (!isPlacedBomb) {
            time += elapsedTime;
        }
        System.out.println(time);
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        playerBoundary.setPosition(x, y, BOMBER_WIDTH, BOMBER_HEIGHT);
        return playerBoundary;
    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        playerBoundary.setPosition(x, y, BOMBER_WIDTH, BOMBER_HEIGHT);
        return playerBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof StaticEntity) {
                if (isColliding(entity)) {
                    return true;
                }
            }
        }
        for (Bomb bomb : Storage.getBombVector()) {
            if (isColliding(bomb) && !bomb.isThroughBomb()) return true;
            if (!isColliding(bomb)) bomb.setThroughBomb(false);
        }
        for (Brick brick : Storage.getBrickVector()) {
            if (isColliding(brick)) return true;
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

        if (keyInput.isPressed(KeyCode.SPACE) && !isPlacedBomb && time >= 60 * elapsedTime) {
            createBomb();
            isPlacedBomb = true;
            time = 0;
        }
        if (!keyInput.isPressed(KeyCode.SPACE) && isPlacedBomb) {
            isPlacedBomb = false;
        }
    }

    private void createBomb() {
        /*numBomb = 0;
        for (int i=0; i<limitBomb; i++) {
            if (bombs[i] != null) {
                numBomb ++;
            }
        }*/
        if (isBombBuff) {
            limitBomb = 2;
        }else {
            limitBomb = 100;
        }
        if (numBomb < limitBomb) {
            int xUnit = (this.x + BOMBER_WIDTH / 2) / Sprite.SCALED_SIZE;
            int yUnit = (this.y + BOMBER_HEIGHT / 2) / Sprite.SCALED_SIZE;
            for (Bomb bomb : Storage.getBombVector()) {
                if (bomb.getGridX() == xUnit && bomb.getGridY() == yUnit) return;
            }
            Bomb bomb = new Bomb(xUnit, yUnit, Sprite.bomb.getFxImage(), game);
            /*for (int i=0; i<limitBomb; i++) {
                if (bombs[i] == null) {
                    bombs[i] = bomb;
                    break;
                }
            }*/
            Storage.addBomb(bomb);
            numBomb = Storage.getNumBomb();
        }
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
                                Sprite.player_up_2, animation, 30);
                    }
                    break;
                case DOWN:
                    currentSprite = Sprite.player_down;
                    if (isMoving()) {
//                    System.out.println("DOWN");
                        currentSprite = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1,
                                Sprite.player_down_2, animation, 30);
                    }
                    break;
                case LEFT:
                    currentSprite = Sprite.player_left;
                    if (isMoving()) {
//                    System.out.println("LEFT");
                        currentSprite = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1,
                                Sprite.player_left_2, animation, 30);
                    }
                    break;
                case RIGHT:
                    currentSprite = Sprite.player_right;
                    if (isMoving()) {
//                    System.out.println("RIGHT");
                        currentSprite = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1,
                                Sprite.player_right_2, animation, 30);
                    }
                    break;
            }
        } else {
            if (!resetAnimation) {
                animation = 0;
                resetAnimation = true;
            }
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

    public Bomb[] getBombs() {
        return bombs;
    }
}
