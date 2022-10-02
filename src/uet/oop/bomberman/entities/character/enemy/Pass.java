package uet.oop.bomberman.entities.character.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.PathFinding.AStarAlgorithm;
import uet.oop.bomberman.entities.character.enemy.PathFinding.PathFindingLv2;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Pass extends Character {
    private int velocity = 1;

    private final static int SPRITE_WIDTH = Sprite.pass_right1.getSpriteHeight();
    private final static int SPRITE_HEIGHT = Sprite.pass_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox passBoundary;
    private GameViewManager game;

    AStarAlgorithm pathFinding;

    public Pass(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        direction = Direction.RIGHT;
        currentSprite = Sprite.minvo_right1;
        moving = true;
        passBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.game = game;
        pathFinding = new AStarAlgorithm(this, game.getBomberman(), game);
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        passBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return passBoundary;
    }

    @Override
    public void update() {
        move();
        animate();
    }

    @Override
    protected void move() {
        pathFinding.updateEnemyDirection();
        switch (direction) {
            case UP:
                moveUp();
                if (checkSafeCollision()) moveDown();
                break;
            case DOWN:
                moveDown();
                if (checkSafeCollision()) moveUp();
                break;
            case RIGHT:
                moveRight();
                if (checkSafeCollision()) moveLeft();
                break;
            case LEFT:
                moveLeft();
                if (checkSafeCollision()) moveRight();
                break;
        }
    }

    public void moveUp() {
        y -= velocity;
    }

    public void moveDown() {
        y += velocity;
    }

    public void moveRight() {
        x += velocity;
    }

    public void moveLeft() {
        x -= velocity;
    }

    @Override
    public void kill() {

    }

    @Override
    public void dead() {

    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        passBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return passBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof StaticEntity) {
                if (isColliding(entity))
                    return true;
            }
        }
        return false;
    }

    public void choosingSprite() {
        switch (direction) {
            case UP:
                currentSprite = Sprite.movingSprite(Sprite.pass_left1, Sprite.pass_right2,
                        Sprite.pass_left3, animation, 60);
                break;
            case DOWN:
                currentSprite = Sprite.movingSprite(Sprite.pass_right1, Sprite.pass_left2,
                        Sprite.pass_right3, animation, 60);
                break;
            case LEFT:
                currentSprite = Sprite.movingSprite(Sprite.pass_left1, Sprite.pass_left2,
                        Sprite.pass_left3, animation, 60);
                break;
            case RIGHT:
                currentSprite = Sprite.movingSprite(Sprite.pass_right1, Sprite.pass_right2,
                        Sprite.pass_right3, animation, 60);
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
