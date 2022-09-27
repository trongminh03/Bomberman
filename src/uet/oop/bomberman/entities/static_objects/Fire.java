package uet.oop.bomberman.entities.static_objects;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.model.RectBoundedBox;

public class Fire extends AnimatedEntity {
    static final int FIRE_WIDTH = 32;
    static final int FIRE_HEIGHT = 32;

    public Fire(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
    }
}
