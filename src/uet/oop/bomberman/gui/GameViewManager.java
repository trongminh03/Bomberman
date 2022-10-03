package uet.oop.bomberman.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.constants.Storage;
import uet.oop.bomberman.entities.character.enemy.Balloom;
import uet.oop.bomberman.entities.character.enemy.Doll;
import uet.oop.bomberman.entities.character.enemy.Minvo;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.static_objects.Brick;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.KeyManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Vector;

public class GameViewManager {
    public static final int WIDTH = 25;
    public static final int HEIGHT = 15;

    public static boolean running;

    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> enemies = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    private Vector<Bomb> bombVector = Storage.getBombVector();
    private Vector<Brick> brickVector = Storage.getBickVector();

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
        initializeStage();
        createKeyListener();
    }

    private void initializeStage() {
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        scene = new Scene(root);

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
//        scene.setOnKeyPressed(event -> {
//            switch (event.getCode()) {
//                case UP:
//                    bomberman.moveUp();
////                    System.out.println("UP");
//                    break;
//                case DOWN:
//                    bomberman.moveDown();
////                    System.out.println("DOWN");
//                    break;
//                case LEFT:
//                    bomberman.moveLeft();
////                    System.out.println("LEFT");
//                    break;
//                case RIGHT:
//                    bomberman.moveRight();
////                    System.out.println("RIGHT");
//                    break;
//            }
//        });
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
            while (i < R) {
                line = bf.readLine();
                for (int j = 0; j < C; j++) {
                    switch (line.charAt(j)) {
                        case '#':
                            object = new Wall(j, i, Sprite.wall.getFxImage());
                            stillObjects.add(object);
                            break;
                        case '*':
                            object = new Brick(j, i, Sprite.brick.getFxImage());
                            Entity grass = new Grass(j, i, Sprite.grass.getFxImage());
                            stillObjects.add(object);
                            stillObjects.add(grass);
                            Storage.addBrick((Brick) object);
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
//        enemies.forEach(Entity::update);
        bomberman.update();
//        bomberman.toString();
        bombVector.forEach(Bomb::update);
        brickVector.forEach(Brick::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Entity entity : stillObjects) {
            if (!(entity instanceof Brick))
                entity.render(gc);
        }
//        enemies.forEach(g -> g.render(gc));
        bomberman.render(gc);
        bombVector.forEach(g -> g.render(gc));
        brickVector.forEach(g -> g.render(gc));
    }

    private void createGameLoop() {
        timer = new AnimationTimer() {
//            long lastTick = 0;
            @Override
            public void handle(long l) {
                /*if (l - lastTick > 1000000000 / FPS) {
                    lastTick = l;
                    render();
                    update();
                    Storage.clearGarbage();
                    if (!bomberman.isAlive()) {
                        mainStage.close();
                        timer.stop();
                        BombermanGame.switchScene(MenuViewManager.getScene());
                    }
                    System.out.println(System.currentTimeMillis());
                }*/
                // Nam's Laptop
                render();
                update();
                if (!bomberman.isAlive()) {
                    mainStage.close();
                    timer.stop();
                    BombermanGame.switchScene(MenuViewManager.getScene());
                }
                Storage.clearGarbage();
            }
        };
        timer.start();
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

    public Scene getScene() {
        return scene;
    }

    public List<Entity> getStillObjects() {
        return stillObjects;
    }
}
