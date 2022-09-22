package uet.oop.bomberman.entities.static_objects;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public class Brick extends StaticEntity {
    RectBoundedBox box;

    public Brick(int x, int y, Image img) {
        super(x, y, img);
        box = new RectBoundedBox(x, y, Sprite.brick.getSpriteWidth(), Sprite.brick.getSpriteWidth());
    }

    @Override
    public void update() {

    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return box;
    }
}
