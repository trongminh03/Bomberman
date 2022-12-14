package uet.oop.bomberman.entities.character;

import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.gui.GameViewManager;

public abstract class Character extends AnimatedEntity {
    protected Direction direction;
    protected boolean alive = true;
    protected boolean moving = false;
    protected GameViewManager game;

    public Character(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        this.game = game;
    }

    @Override
    public abstract void update();

    protected abstract void move();

    public abstract void dead();

    public boolean isAlive() {
        return alive;
    }

    public boolean isMoving() {
        return moving;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public abstract boolean isColliding(Entity other);

}
