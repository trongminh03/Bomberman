package uet.oop.bomberman.entities.item;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public class Portal extends Item {
    public Portal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
}
