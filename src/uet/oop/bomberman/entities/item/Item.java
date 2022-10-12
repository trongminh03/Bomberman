package uet.oop.bomberman.entities.item;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public abstract class Item extends Entity {
    protected final int ITEM_WIDTH = Sprite.SCALED_SIZE;
    protected final int ITEM_HEIGHT = Sprite.SCALED_SIZE;
    protected RectBoundedBox box;

    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        box = new RectBoundedBox(xUnit, yUnit, ITEM_WIDTH, ITEM_HEIGHT);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }

    @Override
    public void update() {

    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return box;
    }
}
