package uet.oop.bomberman.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import uet.oop.bomberman.constants.BombStorage;
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
import java.util.List;
import java.util.Vector;

public class GameViewManager {
    public static final int WIDTH = 25;
    public static final int HEIGHT = 15;

    public static boolean running;

    private GraphicsContext gc;
    private Canvas canvas;

    private List<Entity> entities = new ArrayList<>();
    private static List<Entity> stillObjects = new ArrayList<>();
    private Vector<Bomb> bombVector = BombStorage.getBombVector();

    private Stage mainStage;
//    private Stage menuStage;
    private Group root;
    private Scene scene;

    private AnimationTimer timer;
    private Bomber bomberman;

    private KeyManager keys = new KeyManager();

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
//        this.menuStage = menuStage;
//        this.menuStage.hide();
        createMap();
        createBomberman();
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
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                Entity object;
//                if (j == 0 || j == HEIGHT - 1 || i == 0 || i == WIDTH - 1) {
//                    object = new Wall(i, j, Sprite.wall.getFxImage());
//                }
//                else {
//                    object = new Grass(i, j, Sprite.grass.getFxImage());
//                }
//                stillObjects.add(object);
//            }
//        }
        File file = new File("res/levels/Level2.txt");
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String line = bf.readLine();
            int L, R, C;
            String[] parts = line.split(" ");
            L = Integer.parseInt(parts[0]);
            R = Integer.parseInt(parts[1]);
            C = Integer.parseInt(parts[2]);
            int i = 0;
            while (i < R) {
                line = bf.readLine();
                for (int j = 0; j < line.length(); j++) {
                    Entity object;
                    if (line.charAt(j) == '#') {
                        object = new Wall(j, i, Sprite.wall.getFxImage());
                    } else if (line.charAt(j) == '*') {
                        object = new Brick(j, i, Sprite.brick.getFxImage());
                    } else {
                        object = new Grass(j, i, Sprite.grass.getFxImage());
                    }
                    stillObjects.add(object);
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

    public void createBomberman() {
        bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage(), keys);
        entities.add(bomberman);
    }

    public void update() {
        entities.forEach(Entity::update);
        bombVector.forEach(Bomb::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        bombVector.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }

    private void createGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                render();
                BombStorage.clearGarbage();
            }
        };
        timer.start();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public Scene getScene() {
        return scene;
    }

    public static List<Entity> getStillObjects() {
        return stillObjects;
    }
}
