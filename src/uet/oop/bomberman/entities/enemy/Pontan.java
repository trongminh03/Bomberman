package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemy.PathFinding.AStarAlgorithm;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Pontan extends Enemy {

    private final static int velocity = 2;

    private final static int SPRITE_WIDTH = Sprite.pontan_right1.getSpriteHeight();
    private final static int SPRITE_HEIGHT = Sprite.pontan_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox pontanBoundary;
    private GameViewManager game;

    AStarAlgorithm pathFinding;

    public Pontan(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        direction = Direction.RIGHT;
        brickPass = true;
        currentSprite = Sprite.pontan_right1;
        FINDING_SCOPE = 7;
        moving = true;
        pontanBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        this.game = game;
        pathFinding = new AStarAlgorithm(this, game.getBomberman(), game);
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        pontanBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return pontanBoundary;
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
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        pontanBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return pontanBoundary.checkCollision(otherEntityBoundary);
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
        if (isAlive()) {
            switch (direction) {
                case UP:
                    currentSprite = Sprite.movingSprite(Sprite.pontan_left1, Sprite.pontan_right2,
                            Sprite.pontan_left3, animation, 60);
                    break;
                case DOWN:
                    currentSprite = Sprite.movingSprite(Sprite.pontan_right1, Sprite.pontan_left2,
                            Sprite.pontan_right3, animation, 60);
                    break;
                case LEFT:
                    currentSprite = Sprite.movingSprite(Sprite.pontan_left1, Sprite.pontan_left2,
                            Sprite.pontan_left3, animation, 60);
                    break;
                case RIGHT:
                    currentSprite = Sprite.movingSprite(Sprite.pontan_right1, Sprite.pontan_right2,
                            Sprite.pontan_right3, animation, 60);
                    break;
            }
        }else {
            currentSprite = Sprite.pontan_dead;
            time += elapsedTime;
            if (time == 15 * elapsedTime) {
                game.getEnemieGarbage().add(this);
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }
}
