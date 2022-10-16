package uet.oop.bomberman.entities.character;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.constants.ItemType;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Portal;
import uet.oop.bomberman.entities.item.*;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.gui.WaitViewManager;
import uet.oop.bomberman.input.KeyManager;
import uet.oop.bomberman.model.RectBoundedBox;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class Bomber extends Character {
    private static int velocity;
    private final static int BOMBER_WIDTH = 24;
    private final static int BOMBER_HEIGHT = 32;
    private int numBomb = 0;
    private int limitBomb = 1;
    private int maxBomb = 10;
    private Bomb[] bombs = new Bomb[maxBomb];

    private int numItem = 6;
    private boolean[] items = new boolean[numItem];
    private int numFlameItem = 0; // The number of flame items bomber got
    private int numBombItem = 0; // The number of bomb items bomber got

    private boolean isPlacedBomb = false;   /* Check place bomb: true: bomber placed bomb, can't place bombs after a short amount of time
                                                                 false: bomber no bomb yet, can place bombs now */

    KeyManager keyInput;
    private Sprite currentSprite;
    private RectBoundedBox playerBoundary;
    private boolean fatalHit = false;
    //    private boolean resetAnimation = false;
//    private GameViewManager game;

    AudioManager deadAudio = new AudioManager("res/audio/dead.mp3", AudioManager.GAMEPLAY_MUSIC);
    AudioManager dropBombAudio = new AudioManager("res/audio/dropbomb.wav", AudioManager.GAMEPLAY_MUSIC);
    AudioManager powerupAudio = new AudioManager("res/audio/power_up.wav", AudioManager.GAMEPLAY_MUSIC);

    public Bomber(int x, int y, Image img, KeyManager keyInput, GameViewManager game) {
        super(x, y, img, game);
        this.keyInput = keyInput;
//        this.game = game;
        direction = Direction.RIGHT;
        velocity = 2;
        time = 10;
//        moving = true;
        currentSprite = Sprite.player_right;
        playerBoundary = new RectBoundedBox(x, y, BOMBER_WIDTH, BOMBER_HEIGHT);
        for (int i = 0; i < maxBomb; i++) {
            Bomb bomb = new Bomb(-1, -1, 1, Sprite.bomb.getFxImage(), game);
            bomb.setBombStatus(BombStatus.DESTROY);
            bombs[i] = bomb;
        }
        for (boolean item : items) {
            item = false;
        }
    }


    @Override
    public void update() {
//        if (isMoving()) {
//            move();
//        }
        move();
        animate();
        if (checkFatalCollision() || checkFatalHit()) {
            dead();
        }
        for (Bomb bomb : bombs) {
            if (bomb.getBombStatus() != BombStatus.DESTROY) {
                bomb.update();
            }
        }
        /*if (!isPlacedBomb) {
            time += elapsedTime;
        }*/
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
            //Colliding with wall
            if (entity instanceof Wall) {
                if (isColliding(entity)) {
                    return true;
                }
            }
            //Colliding with brick
            if (entity instanceof Brick) {
                if (!items[ItemType.BRICK_PASS.ordinal()] && isColliding(entity)) {
                    return true;
                }
            }
            ////Colliding with item
            if (entity instanceof Item) {
                if (this.getX() == entity.getX() && this.getY() == entity.getY()) {
                    game.getItemGarbage().add((Item) entity);
                    if (AudioManager.isSoundEnabled(AudioManager.GAMEPLAY_MUSIC)) {
                        powerupAudio.play(1);
                    }
                    powerUp((Item) entity);
                }
            }
            if (entity instanceof Portal) {
                if (this.getX() == entity.getX() && this.getY() == entity.getY()
                        && game.getEnemies().size() == 0) {
                    BombermanGame.numStage++;
                    game.getBackgroundMusic().stop();
                    WaitViewManager waitView = new WaitViewManager();
                    BombermanGame.switchScene(waitView.getWaitScene());
                }
            }
        }
        for (Bomb bomb : bombs) {
            if (bomb.getBombStatus() != BombStatus.DESTROY) {
                if (!items[ItemType.BOMB_PASS.ordinal()]) {
                    if (!isColliding(bomb)) {
                        bomb.setThroughBomb(false);
                    }
                    if (isColliding(bomb) && !bomb.getThroughBomb()) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean checkFatalCollision() {
        for (Entity entity : game.getEnemies()) {
            if (isColliding(entity)) {
                fatalHit = true;
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

        if (keyInput.isPressed(KeyCode.SPACE) && !isPlacedBomb /*&& time >= 60 * elapsedTime*/) {
            createBomb();
            isPlacedBomb = true;
//            time = 0;
        }
        if (!keyInput.isPressed(KeyCode.SPACE) && isPlacedBomb) {
            isPlacedBomb = false;
        }
    }

    private void createBomb() {
        numBomb = 0;
        for (Bomb bomb : bombs) {
            if (bomb.getBombStatus() != BombStatus.DESTROY) {
                numBomb++;
            }
        }

        if (numBomb < limitBomb) {
            if (AudioManager.isSoundEnabled(AudioManager.GAMEPLAY_MUSIC)) {
                dropBombAudio.play(1);
            }
            int xUnit = (this.x + BOMBER_WIDTH / 2) / Sprite.SCALED_SIZE;
            int yUnit = (this.y + BOMBER_HEIGHT / 2) / Sprite.SCALED_SIZE;
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Brick) {
                    if (entity.getGridX() == xUnit && entity.getGridY() == yUnit) return;
                }
                if (entity instanceof Bomb) {
                    if (entity.getGridX() == xUnit && entity.getGridY() == yUnit) return;
                }
            }
            for (int i = 0; i < limitBomb; i++) {
                if (bombs[i].getBombStatus() == BombStatus.DESTROY) {
                    Bomb bomb = new Bomb(xUnit, yUnit, numFlameItem + 1, Sprite.bomb.getFxImage(), game);
                    bombs[i].setBomb(bomb);
//                    if (items[ItemType.BOMB_PASS.ordinal()]) bombs[i].setThroughBomb(true);
                    break;
                }
            }
        }
    }

    @Override
    public void dead() {
        new Timeline(new KeyFrame(
                Duration.millis(1900),
                event -> {
                    alive = false;
                }
        )).play();
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
        if (!fatalHit) {
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
                if (AudioManager.isSoundEnabled(AudioManager.GAMEPLAY_MUSIC)) {
                    deadAudio.play(1);
                }
            }
            velocity = 0;
//            moving = false;
            currentSprite = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2,
                    Sprite.player_dead3, animation, 75);
        }
    }


    @Override
    public void render(GraphicsContext gc) {
        chooseSprite();
        for (Bomb bomb : bombs) {
            if (bomb.getBombStatus() != BombStatus.DESTROY) {
                bomb.render(gc);
            }
        }
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

    public boolean checkFatalHit() {
        return fatalHit;
    }

    public void setFatalHit(boolean fatalHit) {
        this.fatalHit = fatalHit;
    }

    public Bomb[] getBombs() {
        return bombs;
    }

    private void powerUp(Item item) {
        if (item instanceof BombItem) {
            items[ItemType.BOMB.ordinal()] = true;
            limitBomb++;
        }
        if (item instanceof SpeedItem) {
            items[ItemType.SPEED.ordinal()] = true;
            velocity = 4;
        }
        if (item instanceof BombPassItem) {
            items[ItemType.BOMB_PASS.ordinal()] = true;
        }
        if (item instanceof FlameItem) {
            items[ItemType.FLAME.ordinal()] = true;
            numFlameItem++;
        }
        if (item instanceof FlamePassItem) {
            items[ItemType.FLAME_PASS.ordinal()] = true;
        }
        if (item instanceof BrickPassItem) {
            items[ItemType.BRICK_PASS.ordinal()] = true;
        }
    }

    public boolean getFlamePassItem() {
        return items[ItemType.FLAME_PASS.ordinal()];
    }
}
