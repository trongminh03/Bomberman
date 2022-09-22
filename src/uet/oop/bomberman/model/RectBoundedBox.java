package uet.oop.bomberman.model;

import javafx.geometry.Rectangle2D;
import uet.oop.bomberman.graphics.Sprite;

public class RectBoundedBox {
    private int x;
    private int y;
    private int width;
    private int height;
    Rectangle2D boundary;

    public RectBoundedBox(int xUnit, int yUnit, int width, int height) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.width = width;
        this.height = height;
        boundary = new Rectangle2D(x, y, this.width, this.height);
    }

    public Rectangle2D getBoundary() {
        return boundary;
    }

    public void setBoundary(Rectangle2D boundary) {
        this.boundary = boundary;
    }

    public boolean checkCollision(RectBoundedBox box) {
        return box.getBoundary().intersects(this.getBoundary());
    }

    public void setPosition(int x, int y, int _width, int _height) {
        boundary = new Rectangle2D(x, y, _width, _height);
    }
}
