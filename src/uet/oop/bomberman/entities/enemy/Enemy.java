package uet.oop.bomberman.entities.enemy;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.AnimatedEntity;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public abstract class Enemy extends AnimatedEntity {
    protected Direction direction;
    protected boolean alive = true;
    protected boolean moving = false;
    protected boolean brickPass = false;
    protected int SCORE = 0;
    protected int FINDING_SCOPE = 0;
    protected Label labelScore;
    protected GameViewManager game;

    public Enemy(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        this.game = game;
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

    public void showScore(int score) {
        labelScore = new Label();
        labelScore.setText(score + "!");
        labelScore.setFont(Font.font("Comic Sans MS", FontWeight.EXTRA_BOLD, 15));
        labelScore.setTextFill(Color.WHITE);
        labelScore.setLayoutX(getX() + game.getCanvas().getLayoutX());
        labelScore.setLayoutY(getY() + 32 + game.getCanvas().getLayoutY());
        game.getRoot().getChildren().add(labelScore);
        new Timeline(new KeyFrame(
                Duration.millis(700),
                event -> {
                    game.getRoot().getChildren().remove(labelScore);
                }
        )).play();
    }
}
