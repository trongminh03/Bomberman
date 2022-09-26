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
    private int size = 1;
    private BombStatus bombStatus = BombStatus.PLACED;

    Sprite currentSprite;
    RectBoundedBox playerBoundary;

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        chooseSprite();
        animate();
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
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
        timer.schedule(timerDestroy, 2090);
        if (bombStatus == BombStatus.PLACED) {
            currentSprite = Sprite.movingSprite(Sprite.bomb_2, Sprite.bomb_1,
                    Sprite.bomb, animation, 90);
        }else if (bombStatus == BombStatus.EXPLODE){
            currentSprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1,
                    Sprite.bomb_exploded2, animation, 30);
            System.out.println(currentSprite.toString());
        }else {
            this.destroy();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }

    private void destroy() {
        BombStorage.addBombGarbage(this);
    }
}
