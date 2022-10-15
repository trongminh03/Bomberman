package uet.oop.bomberman.entities.enemy;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.constants.BombStatus;
import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemy.PathFinding.RandomMove;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;
import uet.oop.bomberman.model.RectBoundedBox;

public class Doll extends Enemy {

    private int velocity;

    private final static int SPRITE_WIDTH = Sprite.doll_right1.getSpriteWidth();
    private final static int SPRITE_HEIGHT = Sprite.doll_right2.getSpriteHeight();
    private Sprite currentSprite;
    private RectBoundedBox dollBoundary;
//    private GameViewManager game;

    RandomMove randomMove;

    public Doll(int xUnit, int yUnit, Image img, GameViewManager game) {
        super(xUnit, yUnit, img, game);
        direction = Direction.RIGHT;
        currentSprite = Sprite.doll_right1;
        velocity = 2;
        SCORE = 400;
        moving = true;
        dollBoundary = new RectBoundedBox(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        randomMove = new RandomMove(this);
    }

    @Override
    public RectBoundedBox getBoundingBox() {
        dollBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return dollBoundary;
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
        dollBoundary.setPosition(x, y, SPRITE_WIDTH, SPRITE_HEIGHT);
        return dollBoundary.checkCollision(otherEntityBoundary);
    }

    public boolean checkSafeCollision() {
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof Wall || entity instanceof Brick || entity instanceof Bomb) {
                if (entity instanceof Bomb) {
                    Bomb bomb = (Bomb) entity;
                    if (bomb.getBombStatus() != BombStatus.DESTROY) {
                        if (isColliding(bomb)) {
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
                    currentSprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_right2,
                            Sprite.doll_left3, animation, 60);
                    break;
                case DOWN:
                    currentSprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_left2,
                            Sprite.doll_right3, animation, 60);
                    break;
                case LEFT:
                    currentSprite = Sprite.movingSprite(Sprite.doll_left1, Sprite.doll_left2,
                            Sprite.doll_left3, animation, 60);
                    break;
                case RIGHT:
                    currentSprite = Sprite.movingSprite(Sprite.doll_right1, Sprite.doll_right2,
                            Sprite.doll_right3, animation, 60);
                    break;
            }
        }else {
            if (!resetAnimation) {
                animation = 0;
                resetAnimation = true;
            }
            velocity = 0;
            currentSprite = Sprite.movingSprite(Sprite.doll_dead, Sprite.mob_dead1, Sprite.mob_dead2,
                    Sprite.mob_dead3, animation, 40);
            time += elapsedTime;
            if (time == 35 * elapsedTime) {
                showScore(SCORE);
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
