package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public abstract class AnimatedEntity extends Entity {
    protected int animation = 0;
    protected final int MAX_ANIMATION = 300;

    public AnimatedEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    protected void animate() {
        if (animation <= MAX_ANIMATION) animation++;
        else animation = 0; //reset animation
    }

    protected void setAnimation(int animation) {
        this.animation = animation;
    }

    protected int getAnimation() {
        return animation;
    }
}
