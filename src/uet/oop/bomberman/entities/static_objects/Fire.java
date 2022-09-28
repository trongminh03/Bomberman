package uet.oop.bomberman.entities.static_objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.FireType;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public class Fire extends AnimatedEntity {
    static final int FIRE_WIDTH = 32;
    static final int FIRE_HEIGHT = 32;
    private FireType fireType = FireType.DEFAULT;
    Sprite currentSprite;
    RectBoundedBox fireBoundary;

    public Fire(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        fireBoundary = new RectBoundedBox(x, y, FIRE_WIDTH, FIRE_HEIGHT);
    }

    private void chooseSprite() {
//        currentSprite = Sprite.movingSprite(Sprite.explosion_horizontal)
    }

    @Override
    public void update() {

    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
    }


    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }
}
