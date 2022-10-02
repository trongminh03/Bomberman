package uet.oop.bomberman.entities.static_objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Storage;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

import java.util.Timer;
import java.util.TimerTask;

public class Brick extends AnimatedEntity {
    final static int BRICK_WIDTH = 32;
    final static int BRICK_HEIGHT = 32;
    private boolean isAlive = true;
    RectBoundedBox box;
    Sprite currentSprite;

    public Brick(int x, int y, Image img) {
        super(x, y, img);
        box = new RectBoundedBox(x, y, Sprite.brick.getSpriteWidth(), Sprite.brick.getSpriteWidth());
    }


    @Override
    public void update() {
        animate();
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }
    private void chooseSprite() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                destroy();
            }
        };
        if (isAlive) {
            currentSprite = Sprite.brick;
        }else {
            currentSprite = Sprite.movingSprite(Sprite.brick_exploded,
                    Sprite.brick_exploded1, Sprite.brick_exploded2, animation, 60);
            /*Timer timer = new Timer();
            timer.schedule(task, 600);*/
//            destroy();
        }
    }
    @Override
    public void render(GraphicsContext gc) {
        chooseSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        box.setPosition(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        return box;
    }

    private void destroy() {
        Storage.addBrickGarbage(this);
    }
}
