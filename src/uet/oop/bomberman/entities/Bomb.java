package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.ExplosionType;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Bomb extends AnimatedEntity {
    final static int BOMB_WIDTH = 30;
    final static int BOMB_HEIGHT = 30;
    private int size;
    private int maxRight;
    private int maxLeft;
    private int maxUp;
    private int maxDown;
    private BombStatus bombStatus;
    private boolean isThroughBomb = true;   /*Check through bomb:   false: bomber and bomb can't go on top of each other
                                                                    true:  bomber and bomb can go on top of each other*/
    private final double elapsedTime = 1 / 30f;
    private double time;

    Sprite currentSprite;
    RectBoundedBox bombBoundary;
    Explosion[] explosionsRight;
    Explosion[] explosionsLeft;
    Explosion[] explosionsUp;
    Explosion[] explosionsDown;
    AudioManager bombExplode = new AudioManager("res/audio/boom.mp3", AudioManager.GAMEPLAY_MUSIC);
    GameViewManager game;

    public Bomb(int xUnit, int yUnit, int size, Image img, GameViewManager gameViewManager) {
        super(xUnit, yUnit, img);
        this.size = size;
        maxRight = size;
        maxLeft = size;
        maxUp = size;
        maxDown = size;
        explosionsRight = new Explosion[size];
        explosionsLeft = new Explosion[size];
        explosionsUp = new Explosion[size];
        explosionsDown = new Explosion[size];
        bombBoundary = new RectBoundedBox(xUnit, yUnit, BOMB_WIDTH, BOMB_HEIGHT);
        this.game = gameViewManager;
        bombStatus = BombStatus.PLACED;
        explosionInit();
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return bombBoundary;
    }

    private void explosionInit() {
        // Init Right
        {
            //Choose maxRight
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall || entity instanceof Brick) {
                    if (entity.getGridY() == this.getGridY()) {
                        if (entity.getGridX() - this.getGridX() <= maxRight
                                && entity.getGridX() - this.getGridX() > 0) {
                            maxRight = entity.getGridX() - this.getGridX() - 1;
                        }
                    }
                }
            }
            //Init explosion right
            for (int i = 1; i <= maxRight - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX() + i, this.getGridY(),
                        Sprite.explosion_horizontal.getFxImage(), ExplosionType.HORIZONTAL);
                explosionsRight[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX() + maxRight, this.getGridY(),
                    Sprite.explosion_horizontal_right_last.getFxImage(), ExplosionType.LAST_RIGHT);
            explosionsRight[0] = explosion;
        }

        //Init Left
        {
            //Choose maxLeft
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall || entity instanceof Brick) {
                    if (entity.getGridY() == this.getGridY()) {
                        if (this.getGridX() - entity.getGridX() <= maxLeft
                                && this.getGridX() - entity.getGridX() > 0) {
                            maxLeft = this.getGridX() - entity.getGridX() - 1;
                        }
                    }
                }
            }
            //Init explosion left
            for (int i = 1; i <= maxLeft - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX() - i, this.getGridY(),
                        Sprite.explosion_horizontal.getFxImage(), ExplosionType.HORIZONTAL);
                explosionsLeft[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX() - maxLeft, this.getGridY(),
                    Sprite.explosion_horizontal_left_last.getFxImage(), ExplosionType.LAST_LEFT);
            explosionsLeft[0] = explosion;
        }

        //Init Up
        {
            //Choose maxUp
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall || entity instanceof Brick) {
                    if (entity.getGridX() == this.getGridX()) {
                        if (this.getGridY() - entity.getGridY() <= maxUp
                                && this.getGridY() - entity.getGridY() > 0) {
                            maxUp = this.getGridY() - entity.getGridY() - 1;
                        }
                    }
                }
            }
            //Init explosion up
            for (int i = 1; i <= maxUp - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX(), this.getGridY() - i,
                        Sprite.explosion_vertical.getFxImage(), ExplosionType.VERTICAL);
                explosionsUp[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX(), this.getGridY() - maxUp,
                    Sprite.explosion_vertical_top_last.getFxImage(), ExplosionType.LAST_UP);
            explosionsUp[0] = explosion;
        }

        //Init Down
        {
            //Choose maxDown
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall || entity instanceof Brick) {
                    if (entity.getGridX() == this.getGridX()) {
                        if (entity.getGridY() - this.getGridY() <= maxDown
                                && entity.getGridY() - this.getGridY() > 0) {
                            maxDown = entity.getGridY() - this.getGridY() - 1;
                        }
                    }
                }
            }
            //Init explosion down
            for (int i = 1; i <= maxDown - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX(), this.getGridY() + i,
                        Sprite.explosion_vertical.getFxImage(), ExplosionType.VERTICAL);
                explosionsDown[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX(), this.getGridY() + maxDown,
                    Sprite.explosion_vertical_down_last.getFxImage(), ExplosionType.LAST_DOWN);
            explosionsDown[0] = explosion;
        }
//        System.out.println("right = " + maxRight + "\t left = " + maxLeft + "\t up = " + maxUp + "\t down = " + maxDown);
    }

    private void explosion() {
        //Explosion Right
        {
            //Destroy brick right
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Brick) {
                    if (entity.getGridY() == this.getGridY()
                            && entity.getGridX() == this.getGridX() + maxRight + 1
                            && maxRight < size) {
                        ((Brick) entity).setAlive(false);
                    }
                }
            }
            //Destroy enemy right
            for (Entity entity : game.getEnemies()) {
                for (Explosion explosion : explosionsRight) {
                    if (explosion != null) {
                        if (explosion.isColliding(entity)) {
                            ((Enemy) entity).dead();
                        }
                    }
                }
            }
            // Destroy bomberman right
            if (!game.getBomberman().getFlamePassItem()) {
                for (Explosion explosion : explosionsRight) {
                    if (explosion != null) {
                        if (explosion.isColliding(game.getBomberman())) {
                            game.getBomberman().setFatalHit(true);
                        }
                    }
                }
            }
        }
        //Explosion Left
        {
            //Destroy brick left
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Brick) {
                    if (entity.getGridY() == this.getGridY()
                            && entity.getGridX() == this.getGridX() - maxLeft - 1
                            && maxLeft < size) {
                        ((Brick) entity).setAlive(false);
                    }
                }
            }
            //Destroy enemy left
            for (Entity entity : game.getEnemies()) {
                for (Explosion explosion : explosionsLeft) {
                    if (explosion != null) {
                        if (explosion.isColliding(entity)) {
                            ((Enemy) entity).dead();
                        }
                    }
                }
            }
            // Destroy bomberman left
            if (!game.getBomberman().getFlamePassItem()) {
                for (Explosion explosion : explosionsLeft) {
                    if (explosion != null) {
                        if (explosion.isColliding(game.getBomberman())) {
                            game.getBomberman().setFatalHit(true);
                        }
                    }
                }
            }
        }
        //Explosion Up
        {
            //Destroy brick up
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Brick) {
                    if (entity.getGridX() == this.getGridX()
                            && entity.getGridY() == this.getGridY() - maxUp - 1
                            && maxUp < size) {
                        ((Brick) entity).setAlive(false);
                    }
                }
            }
            //Destroy enemy up
            for (Entity entity : game.getEnemies()) {
                for (Explosion explosion : explosionsUp) {
                    if (explosion != null) {
                        if (explosion.isColliding(entity)) {
                            ((Enemy) entity).dead();
                        }
                    }
                }
            }
            // Destroy bomberman up
            if (!game.getBomberman().getFlamePassItem()) {
                for (Explosion explosion : explosionsUp) {
                    if (explosion != null) {
                        if (explosion.isColliding(game.getBomberman())) {
                            game.getBomberman().setFatalHit(true);
                        }
                    }
                }
            }
        }
        //Explosion Down
        {
            //Destroy brick down
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Brick) {
                    if (entity.getGridX() == this.getGridX()
                            && entity.getGridY() == this.getGridY() + maxDown + 1
                            && maxDown < size) {
                        ((Brick) entity).setAlive(false);
                    }
                }
            }
            //Destroy enemy down
            for (Entity entity : game.getEnemies()) {
                for (Explosion explosion : explosionsDown) {
                    if (explosion != null) {
                        if (explosion.isColliding(entity)) {
                            ((Enemy) entity).dead();
                        }
                    }
                }
            }
            // Destroy bomberman down
            if (!game.getBomberman().getFlamePassItem()) {
                for (Explosion explosion : explosionsDown) {
                    if (explosion != null) {
                        if (explosion.isColliding(game.getBomberman())) {
                            game.getBomberman().setFatalHit(true);
                        }
                    }
                }
            }
        }
        //Explosion Center
        {
            //Destroy enemy center
            for (Entity entity : game.getEnemies()) {
                if (this.isColliding(entity)) {
                    ((Enemy) entity).dead();
                }
            }
            // Destroy bomberman center
            if (!game.getBomberman().getFlamePassItem()) {
                if (this.isColliding(game.getBomberman())) {
                    game.getBomberman().setFatalHit(true);
                }
            }
        }
    }

    private void chooseSprite() {
        if (time == 75 * elapsedTime) {
            bombStatus = BombStatus.EXPLODE;
            if (AudioManager.isSoundEnabled(AudioManager.GAMEPLAY_MUSIC)) {
                bombExplode.play(1);
            }
            this.setAnimation(0);
        }
        if (time == 90 * elapsedTime) {
            bombStatus = BombStatus.DESTROY;
            this.x = -1 * Sprite.SCALED_SIZE;
            this.y = -1 * Sprite.SCALED_SIZE;
            time = 0;
        }
        if (bombStatus == BombStatus.PLACED) {
            currentSprite = Sprite.movingSprite(Sprite.bomb_2, Sprite.bomb_1,
                    Sprite.bomb, animation, 45);
        } else if (bombStatus == BombStatus.EXPLODE) {
            currentSprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1,
                    Sprite.bomb_exploded2, Sprite.bomb_exploded1, Sprite.bomb_exploded, animation, 15);
        }
    }

    public boolean getThroughBomb() {
        return isThroughBomb;
    }

    public void setThroughBomb(boolean throughBomb) {
        isThroughBomb = throughBomb;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (bombStatus != BombStatus.DESTROY) {
            chooseSprite();
            if (bombStatus == BombStatus.EXPLODE) {
                for (Explosion explosion : explosionsDown) {
                    if (explosion != null) explosion.render(gc);
                }
                for (Explosion explosion : explosionsUp) {
                    if (explosion != null) explosion.render(gc);
                }
                for (Explosion explosion : explosionsRight) {
                    if (explosion != null) explosion.render(gc);
                }
                for (Explosion explosion : explosionsLeft) {
                    if (explosion != null) explosion.render(gc);
                }
            }
            gc.drawImage(currentSprite.getFxImage(), x, y);
        }
    }

    @Override
    public void update() {
        if (bombStatus == BombStatus.EXPLODE) {
            for (Explosion explosion : explosionsDown) {
                if (explosion != null) explosion.update();
            }
            for (Explosion explosion : explosionsUp) {
                if (explosion != null) explosion.update();
            }
            for (Explosion explosion : explosionsRight) {
                if (explosion != null) explosion.update();
            }
            for (Explosion explosion : explosionsLeft) {
                if (explosion != null) explosion.update();
            }
            explosion();
        }
        animate();
        time += elapsedTime;
    }

    public BombStatus getBombStatus() {
        return bombStatus;
    }

    public void setBombStatus(BombStatus bombStatus) {
        this.bombStatus = bombStatus;
    }

    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        bombBoundary.setPosition(x, y, BOMB_WIDTH, BOMB_HEIGHT);
        return bombBoundary.checkCollision(otherEntityBoundary);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setBomb(Bomb other) {
        this.x = other.x;
        this.y = other.y;
        this.size = other.size;
        this.bombStatus = other.bombStatus;
        this.isThroughBomb = other.isThroughBomb;
        this.explosionsRight = other.explosionsRight;
        this.explosionsLeft = other.explosionsLeft;
        this.explosionsDown = other.explosionsDown;
        this.explosionsUp = other.explosionsUp;
        this.bombBoundary = other.bombBoundary;
        this.maxLeft = other.maxLeft;
        this.maxDown = other.maxDown;
        this.maxUp = other.maxUp;
        this.maxRight = other.maxRight;
    }
}