package uet.oop.bomberman.entities.static_objects;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public abstract class StaticEntity extends Entity {
    public StaticEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
}
