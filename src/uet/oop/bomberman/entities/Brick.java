package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Brick extends AnimatedEntity {
    final static int BRICK_WIDTH = 32;
    final static int BRICK_HEIGHT = 32;
    private boolean isAlive = true;
    RectBoundedBox box;
    Sprite currentSprite;
    private final double elapsedTime = 1/30f;
    private double time = 0;

    private GameViewManager game;

    public Brick(int x, int y, Image img, GameViewManager gameViewManager) {
        super(x, y, img);
        box = new RectBoundedBox(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        this.game = gameViewManager;
    }

    @Override
    public void update() {
        animate();
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    private void chooseSprite() {
        if (!resetAnimation) {
            animation = 0;
            resetAnimation = true;
        }
        currentSprite = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1,
                Sprite.brick_exploded2, animation, 15);
        time += elapsedTime;
        if (time == 15 * elapsedTime) {
            destroy();
        }
    }
    @Override
    public void render(GraphicsContext gc) {
        if (isAlive) {
            super.render(gc);
        } else {
            chooseSprite();
            gc.drawImage(currentSprite.getFxImage(), x, y);
        }
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return box;
    }

    private void destroy() {
        game.getBrickGarbage().add(this);
    }
}
