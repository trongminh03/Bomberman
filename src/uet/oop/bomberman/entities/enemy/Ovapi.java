package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemy.PathFinding.PathFindingLv2;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Ovapi extends Enemy {
    private int velocity;

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
        velocity = 1;
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
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        ovapiBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return ovapiBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof Wall || entity instanceof Bomb) {
                if (entity instanceof Bomb) {
                    Bomb bomb = (Bomb) entity;
                    if (bomb.getBombStatus() != BombStatus.DESTROY) {
                        if (isColliding(bomb) && !bomb.isThroughBomb()) {
                            return true;
                        }
                    }
                } else {
                    if (isColliding(entity))
                        return true;
                }
            }
        }
        return false;
    }

    public void choosingSprite() {
        if (isAlive()) {
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
        }else {
            if (!resetAnimation) {
                animation = 0;
                resetAnimation = true;
            }
            velocity = 0;
            currentSprite = Sprite.movingSprite(Sprite.ovapi_dead, Sprite.mob_dead1, Sprite.mob_dead2,
                    Sprite.mob_dead3, animation, 40);
            time += elapsedTime;
            if (time == 35 * elapsedTime) {
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
