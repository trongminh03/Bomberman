package uet.oop.bomberman.entities.static_objects;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public class Wall extends StaticEntity {
    RectBoundedBox box;

    public Wall(int x, int y, Image img) {
        super(x, y, img);
        box = new RectBoundedBox(x, y, Sprite.wall.getSpriteWidth(), Sprite.wall.getSpriteWidth());
    }

    @Override
    public void update() {

    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return box;
    }

    @Override
    public String toString() {
        return "Wall{x = " + x + ", y = " + y + ", width = "
                + Sprite.wall.getSpriteWidth() + ", height = " + Sprite.wall.getSpriteWidth() + "}";
    }
}
