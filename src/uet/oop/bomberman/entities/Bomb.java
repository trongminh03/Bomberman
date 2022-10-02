package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.Storage;
import uet.oop.bomberman.constants.ExplosionType;
import uet.oop.bomberman.entities.static_objects.Brick;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class Bomb extends AnimatedEntity {
    final static int BOMB_WIDTH = 30;
    final static int BOMB_HEIGHT = 30;
    private int size = 2;
    private int maxSize = 4;
    private BombStatus bombStatus = BombStatus.PLACED;
    /*
    Check through bomb:   false: bomber and bomb can't go on top of each other
                          true:bomber and bomb can go on top of each other
    */
    private boolean isThroughBomb = true;

    Sprite currentSprite;
    RectBoundedBox bombBoundary;
    Explosion[] explosionsRight = new Explosion[maxSize];
    Explosion[] explosionsLeft = new Explosion[maxSize];
    Explosion[] explosionsUp = new Explosion[maxSize];
    Explosion[] explosionsDown = new Explosion[maxSize];

    GameViewManager game;

    public Bomb(int xUnit, int yUnit, Image img, GameViewManager gameViewManager) {
        super(xUnit, yUnit, img);
        bombBoundary = new RectBoundedBox(x, y, BOMB_WIDTH, BOMB_HEIGHT);
        this.game = gameViewManager;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        bombBoundary.setPosition(x, y, BOMB_WIDTH, BOMB_HEIGHT);
        return bombBoundary;
    }

    private void setCurrentSprite(Sprite sprite) {
        if (sprite != null) {
            currentSprite = Sprite.bomb_2;
        } else {
            System.out.println("Missing sprite");
        }
    }

    private void explosion() {
        int maxRight = size;
        int maxLeft = size;
        int maxUp = size;
        int maxDown = size;
        //Choose maxRight
        {
            for (Brick brick : Storage.getBickVector()) {
                if (brick.getGridY() == this.getGridY()) {
                    if (brick.getGridX() - this.getGridX() <= maxRight
                            && brick.getGridX() - this.getGridX() > 0) {
                        maxRight = brick.getGridX() - this.getGridX() - 1;
                    }
                }
            }
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall) {
                    if (entity.getGridY() == this.getGridY()) {
                        if (entity.getGridX() - this.getGridX() <= maxRight
                                && entity.getGridX() - this.getGridX() > 0) {
                            maxRight = entity.getGridX() - this.getGridX() - 1;
                        }
                    }
                }
            }
            //Destroy brick
            for (Brick brick : Storage.getBickVector()) {
                if (brick.getGridY() == this.getGridY()
                && brick.getGridX() == this.getGridX() + maxRight + 1
                && maxRight < size) {
                    brick.setAlive(false);
                }
            }
        }

        //Choose maxLeft
        {
            for (Brick brick : Storage.getBickVector()) {
                if (brick.getGridY() == this.getGridY()) {
                    if (this.getGridX() - brick.getGridX() <= maxLeft
                            && this.getGridX() - brick.getGridX() > 0) {
                        maxLeft = this.getGridX() - brick.getGridX() - 1;
                    }
                }
            }
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall) {
                    if (entity.getGridY() == this.getGridY()) {
                        if (this.getGridX() - entity.getGridX() <= maxLeft
                                && this.getGridX() - entity.getGridX() > 0) {
                            maxLeft = this.getGridX() - entity.getGridX() - 1;
                        }
                    }
                }
            }
        }

        //Choose maxUp
        {
            for (Brick brick : Storage.getBickVector()) {
                if (brick.getGridX() == this.getGridX()) {
                    if (this.getGridY() - brick.getGridY() <= maxUp
                            && this.getGridY() - brick.getGridY() > 0) {
                        maxUp = this.getGridY() - brick.getGridY() - 1;
                    }
                }
            }
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall) {
                    if (entity.getGridX() == this.getGridX()) {
                        if (this.getGridY() - entity.getGridY() <= maxUp
                                && this.getGridY() - entity.getGridY() > 0) {
                            maxUp = this.getGridY() - entity.getGridY() - 1;
                        }
                    }
                }
            }
        }

        //Choose maxDown
        {
            for (Brick brick : Storage.getBickVector()) {
                if (brick.getGridX() == this.getGridX()) {
                    if (brick.getGridY() - this.getGridY() <= maxDown
                            && brick.getGridY() - this.getGridY() > 0) {
                        maxDown = brick.getGridY() - this.getGridY() - 1;
                    }
                }
            }
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall) {
                    if (entity.getGridX() == this.getGridX()) {
                        if (entity.getGridY() - this.getGridY() <= maxDown
                                && entity.getGridY() - this.getGridY() > 0) {
                            maxDown = entity.getGridY() - this.getGridY() - 1;
                        }
                    }
                }
            }
        }

        //Explosion right
        {
            for (int i = 1; i <= maxRight - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX() + i, this.getGridY(),
                        Sprite.explosion_horizontal.getFxImage(), ExplosionType.HORIZONTAL);
                explosion.setAnimation(0);
                explosionsRight[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX() + maxRight, this.getGridY(),
                    Sprite.explosion_horizontal_right_last.getFxImage(), ExplosionType.LAST_RIGHT);
            explosion.setAnimation(0);
            explosionsRight[0] = explosion;
        }

        //Explosion left
        {
            for (int i = 1; i <= maxLeft - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX() - i, this.getGridY(),
                        Sprite.explosion_horizontal.getFxImage(), ExplosionType.HORIZONTAL);
                explosionsLeft[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX() - maxLeft, this.getGridY(),
                    Sprite.explosion_horizontal_left_last.getFxImage(), ExplosionType.LAST_LEFT);
            explosionsLeft[0] = explosion;
        }

        //Explosion up
        {
            for (int i = 1; i <= maxUp - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX(), this.getGridY() - i,
                        Sprite.explosion_vertical.getFxImage(), ExplosionType.VERTICAL);
                explosionsUp[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX(), this.getGridY() - maxUp,
                    Sprite.explosion_vertical_top_last.getFxImage(), ExplosionType.LAST_UP);
            explosionsUp[0] = explosion;
        }

        //Explosion down
        {
            for (int i = 1; i <= maxDown - 1; i++) {
                Explosion explosion = new Explosion(this.getGridX(), this.getGridY() + i,
                        Sprite.explosion_vertical.getFxImage(), ExplosionType.VERTICAL);
                explosionsDown[i] = explosion;
            }
            Explosion explosion = new Explosion(this.getGridX(), this.getGridY() + maxDown,
                    Sprite.explosion_vertical_down_last.getFxImage(), ExplosionType.LAST_DOWN);
            explosionsDown[0] = explosion;
        }
    }

    private void chooseSprite() {
        TimerTask timerFire = new TimerTask() {
            @Override
            public void run() {
                bombStatus = BombStatus.EXPLODE;
            }
        };
        TimerTask timerDestroy = new TimerTask() {
            @Override
            public void run() {
                bombStatus = BombStatus.DESTROY;
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(timerFire, 2000);
        timer.schedule(timerDestroy, 2450);
        if (bombStatus == BombStatus.PLACED) {
            currentSprite = Sprite.movingSprite(Sprite.bomb_2, Sprite.bomb_1,
                    Sprite.bomb, animation, 60);
        } else if (bombStatus == BombStatus.EXPLODE) {
            this.setAnimation(0);
            currentSprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1,
                    Sprite.bomb_exploded2, Sprite.bomb_exploded1, Sprite.bomb_exploded, animation, 30);
            explosion();
        } else {
            this.destroy();
        }
    }

    public boolean isThroughBomb() {
        return isThroughBomb;
    }

    public void setThroughBomb(boolean throughBomb) {
        isThroughBomb = throughBomb;
    }

    @Override
    public void render(GraphicsContext gc) {
        chooseSprite();
        {
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

    @Override
    public void update() {
        {
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
        }
        animate();
    }

    private void destroy() {
        Storage.addBombGarbage(this);
    }

    public RectBoundedBox[] getRecBoundedBox() {
        RectBoundedBox[] rectBoundedBoxes = new RectBoundedBox[4 * maxSize];
        rectBoundedBoxes[0] = this.getBoundingBox();
        for (int i = 1; i <= maxSize; i++) {
            if (explosionsLeft[i] != null) {
                rectBoundedBoxes[i] = explosionsLeft[i].getBoundingBox();
            }
        }
        for (int i = 1; i <= maxSize; i++) {
            if (explosionsRight[i] != null) {
                rectBoundedBoxes[maxSize + i] = explosionsRight[i].getBoundingBox();
            }
        }
        for (int i = 1; i <= maxSize; i++) {
            if (explosionsUp[i] != null) {
                rectBoundedBoxes[2 * maxSize + i] = explosionsUp[i].getBoundingBox();
            }
        }
        for (int i = 1; i <= maxSize; i++) {
            if (explosionsDown[i] != null) {
                rectBoundedBoxes[3 * maxSize + i] = explosionsDown[i].getBoundingBox();
            }
        }
        return rectBoundedBoxes;
    }
}
