package uet.oop.bomberman.entities.character.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.PathFinding.RandomMove;
import uet.oop.bomberman.entities.static_objects.Brick;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Balloom extends Character {
    private final static int velocity = 1;

    private final static int SPRITE_WIDTH = Sprite.balloom_right1.getSpriteWidth();
    private final static int SPRITE_HEIGHT = Sprite.balloom_right1.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox balloomBoundary;
    private GameViewManager game;
    private RandomMove randomMove;

    public Balloom(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img);
        this.game = game;
        direction = Direction.RIGHT;
        currentSprite = Sprite.balloom_right1;
        moving = true;
        balloomBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        randomMove = new RandomMove(this);
    }

    @Override
    public void update() {
        move();
        animate();
    }

    @Override
    protected void move() {
//        System.out.println(toString());
        randomMove.setRandomDirection();
        switch (direction) {
            case UP:
                moveUp();
                if (checkSafeCollision()) moveDown();
                break;
            case DOWN:
                moveDown();
                if (checkSafeCollision()) moveUp();
                break;
            case LEFT:
                moveLeft();
                if (checkSafeCollision()) moveRight();
                break;
            case RIGHT:
                moveRight();
                if (checkSafeCollision()) moveLeft();
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
        this.alive = false;
    }

    @Override
    public boolean isColliding(Entity other) {
        RectBoundedBox otherEntityBoundary = (RectBoundedBox) other.getBoundingBox();
        balloomBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return balloomBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof StaticEntity) {
                if (isColliding(entity))
                    return true;
            }
            if (entity instanceof Brick) {
                Brick brick = (Brick) entity;
                if (isColliding(brick)) return true;
            }
        }
        for (Bomb bomb : game.getBomberman().getBombs()) {
            if (isColliding(bomb)) return true;
        }
        return false;
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        balloomBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return balloomBoundary;
    }

    public void choosingSprite() {
        switch (direction) {
            case UP:
                currentSprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_right2,
                        Sprite.balloom_left3, animation, 60);
                break;
            case DOWN:
                currentSprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_left2,
                        Sprite.balloom_right3, animation, 60);
                break;
            case LEFT:
                currentSprite = Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2,
                        Sprite.balloom_left3, animation, 60);
                break;
            case RIGHT:
                currentSprite = Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2,
                        Sprite.balloom_right3, animation, 60);
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        choosingSprite();
        gc.drawImage(currentSprite.getFxImage(), x, y);
    }

    @Override
    public String toString() {
        return "Balloom{x = " + x + ", y = " + y + "}";
    }
}
