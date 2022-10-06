package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public class Brick extends AnimatedEntity {
    RectBoundedBox box;
    private Sprite currentSprite;

    public Brick(int x, int y, Image img) {
        super(x, y, img);
        box = new RectBoundedBox(x, y, Sprite.brick.getSpriteWidth(), Sprite.brick.getSpriteWidth());
        currentSprite = Sprite.brick;
    }

    @Override
    public void update() {
        animate();
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return box;
    }
}
