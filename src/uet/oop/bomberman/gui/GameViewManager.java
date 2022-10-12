package uet.oop.bomberman.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.enemy.*;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.item.*;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.KeyManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameViewManager {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 15;

    public static boolean running;

    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> enemies = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    private List<Brick> brickGarbage = new ArrayList<>();
    private List<Enemy> enemieGarbage = new ArrayList<>();
    private List<Item> itemGarbage = new ArrayList<>();

    private Stage mainStage;
    //    private Stage menuStage;
    private Group root;
    private Scene scene;

    private AnimationTimer timer;
    private int L, R, C;

    private double t = 0;
    private final static double FPS = 62;
    private KeyManager keys = new KeyManager();
    AudioManager backgroundMusic = new AudioManager("res/audio/background_song.mp3");
    private Bomber bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage(), keys, this);

    public GameViewManager() {
        createNewGame();
        initializeStage();
        createKeyListener();
//        createMap();
    }

    private void initializeStage() {
        canvas = new Canvas(Sprite.SCALED_SIZE * getColumns(), Sprite.SCALED_SIZE * getRows());
        gc = canvas.getGraphicsContext2D();
        // Tao root container
        root = new Group();
        root.getChildren().add(canvas);
        // Tao scene
        scene = new Scene(root, Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);

        // Tao stage
        mainStage = new Stage();
        mainStage.setScene(scene);
    }

    public void createNewGame() {
        enemies = new ArrayList<>();
        stillObjects = new ArrayList<>();
//        this.menuStage = menuStage;
//        this.menuStage.hide();
        createMap();
//        createBomberman();
        createGameLoop();
        if (AudioManager.isSoundEnabled()) {
            playBackgroundMusic();
        } else backgroundMusic.stop();
//        mainStage.show();
    }

    private void createKeyListener() {
        scene.setOnKeyPressed(event -> keys.pressed(event));
        scene.setOnKeyReleased(event -> keys.released(event));
    }

    public void createMap() {
        File file = new File("res/levels/Level2.txt");
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line = bf.readLine();
//            int L, R, C;
            String[] parts = line.split(" ");
            L = Integer.parseInt(parts[0]);
            R = Integer.parseInt(parts[1]);
            C = Integer.parseInt(parts[2]);
            int i = 0;
            Entity object;
            Entity enemy;
            Entity grass;
            Entity brick;
            List<String> lines = new ArrayList<String>();
            // create map
            while (i < R) {
                line = bf.readLine();
                lines.add(line);
                for (int j = 0; j < C; j++) {
                    switch (line.charAt(j)) {
                        case '#':
                            object = new Wall(j, i, Sprite.wall.getFxImage());
                            stillObjects.add(object);
                            break;
                        case '*':
                            object = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            break;
                        case 'p':
//                            bomberman = new Bomber(j, i, Sprite.player_right.getFxImage(), keys, this);
                            bomberman.setPosition(j, i);
//                            System.out.println("Create bomberman");
                            object = new Grass(j, i, Sprite.grass.getFxImage());
//                            enemies.add(bomberman);
                            stillObjects.add(object);
                            break;
                        case 'f':
                            object = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
                            brick = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            stillObjects.add(brick);
                            break;
                        case 'b':
                            object = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
                            brick = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            stillObjects.add(brick);
                            break;
                        case 's':
                            object = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
                            brick = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            stillObjects.add(brick);
                            break;
                        case 'W':
                            object = new BrickPassItem(j, i, Sprite.powerup_wallpass.getFxImage());
                            brick = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            stillObjects.add(brick);
                            break;
                        case 'B':
                            object = new BombPassItem(j, i, Sprite.powerup_bombpass.getFxImage());
                            brick = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            stillObjects.add(brick);
                            break;
                        case 'F':
                            object = new FlamePassItem(j, i, Sprite.powerup_flamepass.getFxImage());
                            brick = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            stillObjects.add(brick);
                            break;
                        case 'P':
                            object = new Portal(j, i, Sprite.portal.getFxImage());
                            brick = new Brick(j, i, Sprite.brick.getFxImage(), this);
                            grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(grass);
                            stillObjects.add(object);
                            stillObjects.add(brick);
                            break;
                        case '1':
                            enemy = new Balloom(j, i, Sprite.balloom_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                            break;
                        case '2':
                            enemy = new Oneal(j, i, Sprite.oneal_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                            break;
                        case '3':
                            enemy = new Doll(j, i, Sprite.doll_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                            break;
                        case '4':
                            enemy = new Minvo(j, i, Sprite.minvo_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                            break;
                        case '5':
                            enemy = new Ovapi(j, i, Sprite.ovapi_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                            break;
                        case '6':
                            enemy = new Pass(j, i, Sprite.pass_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                            break;
                        case '7':
                            enemy = new Kondoria(j, i, Sprite.kondoria_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                            break;
                        case '8':
                            enemy = new Pontan(j, i, Sprite.pontan_right1.getFxImage(), this);
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            enemies.add(enemy);
                            stillObjects.add(object);
                        default:
                            object = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(object);
                            break;
                    }
                }
                i++;
            }

            for (Bomb bomb : bomberman.getBombs()) {
                stillObjects.add(bomb);
            }
            bf.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        enemies.forEach(Entity::update);
        for (Entity entity : stillObjects) {
            if (entity instanceof Brick) {
                entity.update();
            }
        }
        bomberman.update();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        bomberman.render(gc);
        enemies.forEach(g -> g.render(gc));
    }

    private void createGameLoop() {
        timer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long l) {
                if (l - lastTick > 1000000000 / FPS) {
                    lastTick = l;
                    moveBackground();
                    render();
                    update();
                    if (bomberman.checkFatalHit()) {
                        backgroundMusic.stop();
                    }
                    if (!bomberman.isAlive()) {
                        mainStage.close();
                        timer.stop();
                        MenuViewManager menuView = new MenuViewManager();
                        BombermanGame.switchScene(MenuViewManager.getScene());
                    }
//                        BombermanGame.switchScene(MenuViewManager.getScene());

                    clearGarbage();
                }
//                System.out.println(System.currentTimeMillis());
            }
        };
        timer.start();
    }

    public void playBackgroundMusic() {
        backgroundMusic.play(MediaPlayer.INDEFINITE);
    }

    public List<Entity> getEnemies() {
        return enemies;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public Bomber getBomberman() {
        return bomberman;
    }

    public int getRows() {
        return R;
    }

    public int getColumns() {
        return C;
    }

    public void moveBackground() {
        int midHorizontalPosition = (WIDTH * Sprite.SCALED_SIZE) / 2;
        // check if bomberman is in the middle of the screen width
        if (bomberman.getX() >= midHorizontalPosition
                && bomberman.getX() <= getColumns() * Sprite.SCALED_SIZE - midHorizontalPosition) {
            // move background upon bomberman position
            canvas.setLayoutX(midHorizontalPosition - bomberman.getX());
        } else if (bomberman.getX() < midHorizontalPosition) { // set camera upon bomberman current position
            canvas.setLayoutX(0);
        } else { // set camera upon bomberman position
            canvas.setLayoutX((WIDTH - getColumns()) * Sprite.SCALED_SIZE);
        }

        // check if bomberman is in the middle of the screen height
        int midVerticalPosition = (HEIGHT * Sprite.SCALED_SIZE) / 2;
        if (bomberman.getY() >= midVerticalPosition
                && bomberman.getY() <= getRows() * Sprite.SCALED_SIZE - midVerticalPosition) {
            // move background upon bomberman position
            canvas.setLayoutY(midVerticalPosition - bomberman.getY());
        } else if (bomberman.getY() < midVerticalPosition) { // set camera upon bomberman position
            canvas.setLayoutY(0);
        } else { // set camera upon bomberman position
            canvas.setLayoutY((HEIGHT - getRows()) * Sprite.SCALED_SIZE);
        }
    }

    public List<Entity> getStillObjects() {
        return stillObjects;
    }

    public List<Item> getItemGarbage() { return itemGarbage;}

    public Scene getScene() {
        return scene;
    }

    public List<Brick> getBrickGarbage() {
        return brickGarbage;
    }
    public List<Enemy> getEnemieGarbage() {
        return enemieGarbage;
    }

    private void clearGarbage() {
        if (brickGarbage.size() != 0) {
            stillObjects.removeAll(brickGarbage);
            brickGarbage.clear();
        }
        if (enemieGarbage.size() != 0) {
            enemies.removeAll(enemieGarbage);
            enemieGarbage.clear();
        }
        if (itemGarbage.size() != 0) {
            stillObjects.removeAll(itemGarbage);
            itemGarbage.clear();
        }
    }
}
