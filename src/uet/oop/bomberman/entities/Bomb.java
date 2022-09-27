package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.BombStorage;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends AnimatedEntity {
    final static int BOMB_WIDTH = 30;
    final static int BOMB_HEIGHT = 30;
    private int size = 1;
    private BombStatus bombStatus = BombStatus.PLACED;
    /*
    Check through bomb:   false: bomber and bomb can't go on top of each other
                          true:bomber and bomb can go on top of each other
    */
    private boolean isThroughBomb = true;

    Sprite currentSprite;
    RectBoundedBox bombBoundary;

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        bombBoundary = new RectBoundedBox(x, y, BOMB_WIDTH, BOMB_HEIGHT);
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
        timer.schedule(timerDestroy, 2540);
        if (bombStatus == BombStatus.PLACED) {
            currentSprite = Sprite.movingSprite(Sprite.bomb_2, Sprite.bomb_1,
                    Sprite.bomb, animation, 90);
        }else if (bombStatus == BombStatus.EXPLODE){
            currentSprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1,
                    Sprite.bomb_exploded2, Sprite.bomb_exploded1, Sprite.bomb_exploded, animation, 30);
            System.out.println(currentSprite.toString());
        }else {
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
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }

    @Override
    public void update() {
        chooseSprite();
        animate();
    }

    private void destroy() {
        BombStorage.addBombGarbage(this);
    }
}
