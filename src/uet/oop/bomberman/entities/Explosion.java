package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.ExplosionType;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.model.RectBoundedBox;

public class Explosion extends AnimatedEntity {
    static final int EXPLOSION_WIDTH = 32;
    static final int EXPLOSION_HEIGHT = 32;
    private ExplosionType explosionType = ExplosionType.DEFAULT;
    Sprite currentSprite;
    RectBoundedBox explosionBoundary;

    public Explosion(int xUnit, int yUnit, Image img, ExplosionType explosionType) {
        super(xUnit, yUnit, img);
        this.explosionType = explosionType;
        explosionBoundary = new RectBoundedBox(x, y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
    }

    private void setCurrentSprite(Sprite sprite) {
        if (sprite != null) {
            switch (explosionType) {
                case LAST_DOWN:
                    currentSprite = Sprite.explosion_vertical_down_last;
                    break;
                case LAST_UP:
                    currentSprite = Sprite.explosion_vertical_top_last;
                    break;
                case LAST_LEFT:
                    currentSprite = Sprite.explosion_horizontal_left_last;
                    break;
                case LAST_RIGHT:
                    currentSprite = Sprite.explosion_horizontal_right_last;
                    break;
                case HORIZONTAL:
                    currentSprite = Sprite.explosion_horizontal;
                    break;
                case VERTICAL:
                    currentSprite = Sprite.explosion_vertical;
                    break;
                default:
                    break;

            }
        } else {
            System.out.println("Missing sprite");
        }
    }

    private void chooseSprite() {
        switch (explosionType) {
            case LAST_DOWN:
                currentSprite = Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1,
                        Sprite.explosion_vertical_down_last2, Sprite.explosion_vertical_down_last1,
                        Sprite.explosion_vertical_down_last, animation, 30);
                break;
            case LAST_UP:
                currentSprite = Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1,
                        Sprite.explosion_vertical_top_last2, Sprite.explosion_vertical_top_last1,
                        Sprite.explosion_vertical_top_last, animation, 30);
                break;
            case LAST_LEFT:
                currentSprite = Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1,
                        Sprite.explosion_horizontal_left_last2, Sprite.explosion_horizontal_left_last1,
                        Sprite.explosion_horizontal_left_last, animation, 30);
                System.out.println(currentSprite);
                break;
            case LAST_RIGHT:
                currentSprite = Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1,
                        Sprite.explosion_horizontal_left_last2, Sprite.explosion_horizontal_left_last1,
                        Sprite.explosion_horizontal_right_last, animation, 30);
                break;
            case HORIZONTAL:
                currentSprite = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1,
                        Sprite.explosion_horizontal2, Sprite.explosion_horizontal1,
                        Sprite.explosion_horizontal, animation, 30);
                break;
            case VERTICAL:
                currentSprite = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1,
                        Sprite.explosion_vertical2, Sprite.explosion_vertical1,
                        Sprite.explosion_vertical, animation, 30);
                break;
            default:
                break;
        }
    }

    @Override
    public void update() {
        animate();
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        explosionBoundary.setPosition(x, y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT);
        return explosionBoundary;
    }


    @Override
    public void render(GraphicsContext gc) {
        chooseSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
