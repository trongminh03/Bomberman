package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public abstract class StaticEntity extends Entity {
    public StaticEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
}
