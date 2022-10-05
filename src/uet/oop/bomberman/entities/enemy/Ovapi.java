package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.enemy.PathFinding.PathFindingLv2;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Ovapi extends Enemy {
    private final static int velocity = 1;

    private final static int SPRITE_WIDTH = Sprite.ovapi_right1.getSpriteHeight();
    private final static int SPRITE_HEIGHT = Sprite.ovapi_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox ovapiBoundary;
    private GameViewManager game;

    PathFindingLv2 pathFinding;

    public Ovapi(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        direction = Direction.RIGHT;
        currentSprite = Sprite.minvo_right1;
        moving = true;
        ovapiBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.game = game;
        pathFinding = new PathFindingLv2(this, game.getBomberman(), game);
        FINDING_SCOPE = 5;
        brickPass = true;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        ovapiBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return ovapiBoundary;
    }

    @Override
    public void update() {
        move();
        animate();
    }

    @Override
    protected void move() {
        pathFinding.updateEnemyDirectionBrickPass();
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
    public void dead() {

    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        ovapiBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return ovapiBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof Wall) {
                if (isColliding(entity))
                    return true;
            }
        }
        return false;
    }

    public void choosingSprite() {
        switch (direction) {
            case UP:
                currentSprite = Sprite.movingSprite(Sprite.ovapi_left1, Sprite.ovapi_right2,
                        Sprite.ovapi_left3, animation, 60);
                break;
            case DOWN:
                currentSprite = Sprite.movingSprite(Sprite.ovapi_right1, Sprite.ovapi_left2,
                        Sprite.ovapi_right3, animation, 60);
                break;
            case LEFT:
                currentSprite = Sprite.movingSprite(Sprite.ovapi_left1, Sprite.ovapi_left2,
                        Sprite.ovapi_left3, animation, 60);
                break;
            case RIGHT:
                currentSprite = Sprite.movingSprite(Sprite.ovapi_right1, Sprite.ovapi_right2,
                        Sprite.ovapi_right3, animation, 60);
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
