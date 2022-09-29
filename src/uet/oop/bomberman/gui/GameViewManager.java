package uet.oop.bomberman.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.enemy.*;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
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
//    private List<Entity> entities = new ArrayList<>();
    private List<Entity> enemies = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    private Stage mainStage;
    //    private Stage menuStage;
    private Group root;
    private Scene scene;

    private AnimationTimer timer;
    private int L, R, C;

    private double t = 0;
    private final static double FPS = 62;
    private KeyManager keys = new KeyManager();
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
//        mainStage.show();
    }

    private void createKeyListener() {
        scene.setOnKeyPressed(event -> keys.pressed(event));
        scene.setOnKeyReleased(event -> keys.released(event));
    }

    public void createMap() {
        File file = new File("res/levels/Level4.txt");
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
                            object = new Brick(j, i, Sprite.brick.getFxImage());
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
            bf.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void createBomberman() {
//        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage(), keys);
//        entities.add(bomberman);
//    }

    public void update() {
        enemies.forEach(Entity::update);
        bomberman.update();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        enemies.forEach(g -> g.render(gc));
        bomberman.render(gc);
    }

    private void createGameLoop() {
        timer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long l) {
                // Minh's laptop
//                if (lastTick == 0) {
//                    lastTick = l;
//                    render();
//                    update();
//                }

                if (l - lastTick > 1000000000 / FPS) {
                    lastTick = l;
                    moveBackground();
                    render();
                    update();
                    if (!bomberman.isAlive()) {
                        mainStage.close();
                        timer.stop();
                        BombermanGame.switchScene(MenuViewManager.getScene());
                    }
                    System.out.println(canvas.getLayoutX() + " " + canvas.getLayoutY());
//                t += 0.016;
//
//                if (t > 0.02) {
//                    render();
//                    update();
//                    t = 0;
//                }

                    // Nam's Laptop
//                render();
//                update();
                }
            }
        };
        timer.start();
    }
        public Scene getScene () {
            return scene;
        }

        public List<Entity> getStillObjects () {
            return stillObjects;
        }

        public List<Entity> getEnemies () {
            return enemies;
        }

        public Stage getMainStage () {
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
    }
