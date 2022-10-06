package uet.oop.bomberman.entities.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.model.RectBoundedBox;

public abstract class Enemy extends AnimatedEntity {
    protected Direction direction;
    protected boolean alive = true;
    protected boolean moving = false;
    protected boolean brickPass = false;
    protected int SCORE = 0;
    protected int FINDING_SCOPE = 0;
    protected final double elapsedTime = 1/30f;
    protected double time = 0;

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public abstract void update();

    protected abstract void move();

    public void dead() {
        alive = false;
    };

    public boolean isAlive() {
        return alive;
    }

    public boolean isMoving() {
        return moving;
    };

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public abstract boolean isColliding(Entity other);

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getFindingScope() {
        return FINDING_SCOPE;
    }

    public int getScore() {
        return SCORE;
    }

    public boolean canBrickPass() {
        return brickPass;
    }
}
