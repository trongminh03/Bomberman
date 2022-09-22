package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public class Bomb extends AnimatedEntity {
    private int size = 1;
    private boolean isFire = false;

    Sprite currentSprite;
    RectBoundedBox playerBoundary;

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        animate();
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        return null;
    }

    private void setCurrentSprite(Sprite sprite) {
        if (sprite != null) {
            currentSprite = Sprite.bomb_2;
        } else {
            System.out.println("Missing sprite");
        }
    }

    private void chooseSprite() {
        if (isFire) {
            currentSprite = Sprite.movingSprite(Sprite.bomb_2, Sprite.bomb_1,
                    Sprite.bomb, animation, 30);
        }else {
            currentSprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1,
                    Sprite.bomb_exploded2, animation, 30);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        chooseSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
