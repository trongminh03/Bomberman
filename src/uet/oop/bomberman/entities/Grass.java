package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.model.RectBoundedBox;

public class Grass extends Entity {

    public Grass(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
    }
}
