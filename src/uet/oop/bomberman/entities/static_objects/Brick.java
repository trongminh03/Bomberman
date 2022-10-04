package uet.oop.bomberman.entities.static_objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Brick extends AnimatedEntity {
    final static int BRICK_WIDTH = 32;
    final static int BRICK_HEIGHT = 32;
    private boolean isAlive = true;
    RectBoundedBox box;
    Sprite currentSprite;
    private double elapedTime = 1/30f;
    private double time = 0;

    private GameViewManager game;

    public Brick(int x, int y, Image img, GameViewManager gameViewManager) {
        super(x, y, img);
        box = new RectBoundedBox(x, y, Sprite.brick.getSpriteWidth(), Sprite.brick.getSpriteWidth());
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

        currentSprite = Sprite.movingSprite(Sprite.brick_exploded,
                Sprite.brick_exploded1, Sprite.brick_exploded2, animation, 30);
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
        game.getStillObjects().remove(this);
    }
}
