package uet.oop.bomberman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uet.oop.bomberman.constants.GlobalConstants;
import uet.oop.bomberman.gui.MenuViewManager;

public class BombermanGame extends Application {
    private static Stage stage;

//    public static final int WIDTH = 25;
//    public static final int HEIGHT = 15;
//
//    public static boolean running;
//
//    private GraphicsContext gc;
//    private Canvas canvas;
//    private List<Entity> entities = new ArrayList<>();
//    private List<Entity> stillObjects = new ArrayList<>();


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        MenuViewManager menuView = new MenuViewManager();
        stage.setScene(menuView.getMenuScene());
        stage.setTitle(GlobalConstants.GAME_NAME + GlobalConstants.GAME_VERSION);
//        stage = menuView.getMenuStage();
        stage.show();

//        // Tao Canvas
//        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
//        gc = canvas.getGraphicsContext2D();
//
//        // Tao root container
//        Group root = new Group();
//        root.getChildren().add(canvas);
//
//        // Tao scene
//        Scene scene = new Scene(root);
//
//        scene.setOnKeyPressed(event -> {
//            switch (event.getCode()) {
//                case UP:
//                    System.out.println("UP");
//                    break;
//                case DOWN:
//                    System.out.println("DOWN");
//                    break;
//                case LEFT:
//                    System.out.println("LEFT");
//                    break;
//                case RIGHT:
//                    System.out.println("RIGHT");
//                    break;
//            }
//        });
//
//        // Them scene vao stage
//        stage.setScene(scene);
//        stage.setTitle("Bomberman v1.0");
//        stage.show();
//
//        AnimationTimer timer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                render();
//                update();
//            }
//        };
//        timer.start();
//
//        createMap();
//
//        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
//        entities.add(bomberman);
    }

    public static void switchScene(Scene scene) {
        stage.setScene(scene);
    }
//
//    public void createMap() {
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
//    }
//
//    public void update() {
//        entities.forEach(Entity::update);
//    }
//
//    public void render() {
//        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//        stillObjects.forEach(g -> g.render(gc));
//        entities.forEach(g -> g.render(gc));
//    }
}
