package uet.oop.bomberman.gui;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.enemy.*;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.item.*;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.info.Score;
import uet.oop.bomberman.info.Timer;
import uet.oop.bomberman.input.KeyManager;
import uet.oop.bomberman.model.InfoLabel;

import java.awt.*;
import java.io.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class GameViewManager {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 15;

    private GraphicsContext gc;
    private Canvas canvas;

    Pane pane;
    private List<Entity> enemies = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    private List<Brick> brickGarbage = new ArrayList<>();
    private List<Enemy> enemiesGarbage = new ArrayList<>();
    private List<Item>  itemGarbage = new ArrayList<>();
    private PauseViewManager pauseViewManager = new PauseViewManager(this);
    private Stage mainStage;
    private Group root;
    private Scene scene;
    private boolean isCanPressPause = true;
    private AnimationTimer animationTimer;
    private int L, R, C;

    private double t = 0;
    private final static double FPS = 62;
    private KeyManager keys = new KeyManager();
    AudioManager backgroundMusic = new AudioManager("res/audio/background_song.mp3",
            AudioManager.BACKGROUND_MUSIC);
    private Bomber bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage(), keys, this);


    private Text level, score, time, lives;
    private Timer timer;

    private boolean TIMEUP = false;

    public GameViewManager(int numStage) {
        createNewGame(numStage);
        initializeStage();
        createKeyListener();
    }

    private void initializeStage() {
        createGameInfo();
        canvas = new Canvas(Sprite.SCALED_SIZE * getColumns(), Sprite.SCALED_SIZE * getRows());
        canvas.setTranslateY(32);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        root = new Group();
        root.getChildren().add(canvas);
        root.getChildren().add(pane);
        // Tao scene
        scene = new Scene(root, Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);

        // Tao stage
        mainStage = new Stage();
        mainStage.setScene(scene);
    }

    private void createGameInfo() {
        HBox gameInfoBox = new HBox();
        gameInfoBox.setSpacing(100);
        level = new Text("Level: " + BombermanGame.numStage);
//        level.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        try {
            level.setFont(Font.loadFont(new FileInputStream(InfoLabel.font), 27));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        level.setFill(Color.WHITE);

        score = new Text();
        score.setText("Score: " + Score.getScore());
//        score.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        try {
            score.setFont(Font.loadFont(new FileInputStream(InfoLabel.font), 27));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        score.setFill(Color.WHITE);

        HBox livesBox = new HBox();
        livesBox.setSpacing(10);
        ImageView bomberIcon = new ImageView(new Image("/model/img/bomberman.png"));
        lives = new Text(Integer.toString(Bomber.LIVES));
//        lives.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        try {
            lives.setFont(Font.loadFont(new FileInputStream(InfoLabel.font), 27));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        lives.setFill(Color.WHITE);
        livesBox.getChildren().addAll(bomberIcon, lives);
        livesBox.setAlignment(Pos.CENTER);

        HBox timerBox = new HBox();
        timerBox.setSpacing(10);
        ImageView timerView = new ImageView(new Image("/model/img/stopwatch.png"));
        time = new Text();
        time.setText(Long.toString(timer.getTimeValue()));
//        time.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        try {
            time.setFont(Font.loadFont(new FileInputStream(InfoLabel.font), 27));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        time.setFill(Color.WHITE);
        timerBox.getChildren().addAll(timerView, time);
        timerBox.setAlignment(Pos.CENTER);

        gameInfoBox.getChildren().addAll(level, score, livesBox, timerBox);
        gameInfoBox.setAlignment(Pos.CENTER);
        gameInfoBox.setPadding(new Insets(5, 0, 0, 20));


        pane = new Pane();
        pane.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
//        pane.getChildren().addAll(level, score, bomberIcon, lives, timerView, time);
        pane.getChildren().add(gameInfoBox);
        pane.setMinSize(640, 32);
        pane.setMaxSize(640, 480);
    }

    public void createNewGame(int numStage) {
        enemies = new ArrayList<>();
        stillObjects = new ArrayList<>();
        timer = new Timer();
        TIMEUP = false;
//        this.menuStage = menuStage;
//        this.menuStage.hide();
        createMap(numStage);
//        createBomberman();
        createGameLoop();
        if (AudioManager.isSoundEnabled(AudioManager.BACKGROUND_MUSIC)) {
            playBackgroundMusic();
        } else backgroundMusic.stop();
    }

    private void createKeyListener() {
        scene.setOnKeyPressed(event -> keys.pressed(event));
        scene.setOnKeyReleased(event -> keys.released(event));
    }

    public void createMap(int numStage) {
        File file = new File("res/levels/Level" + numStage + ".txt");
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
        pauseGame();
        if (!pauseViewManager.isShow()) {
//            System.out.println();
            enemies.forEach(Entity::update);
            for (Entity entity : stillObjects) {
                if (entity instanceof Brick) {
                    entity.update();
                }
            }
            bomberman.update();
            updateGameInfo();
            timer.update();
            if (root.getChildren().contains(pauseViewManager)) {
                root.getChildren().remove(pauseViewManager);
            }
        }else {
            if (!root.getChildren().contains(pauseViewManager)) {
                root.getChildren().add(pauseViewManager);
            }
//            backgroundMusic.stop();
        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        bomberman.render(gc);
        enemies.forEach(g -> g.render(gc));
    }

    private void createGameLoop() {
        animationTimer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long l) {
                if (l - lastTick > 1000000000 / FPS) {
                    lastTick = l;
                    moveBackground();
                    render();
                    update();
                    afterDead();
                    clearGarbage();
                }
            }
        };
        animationTimer.start();
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
        int midVerticalPosition = ((HEIGHT - 1) * Sprite.SCALED_SIZE) / 2;
        if (bomberman.getY() >= midVerticalPosition
                && bomberman.getY() <= getRows() * Sprite.SCALED_SIZE - midVerticalPosition) {
            // move background upon bomberman position
            canvas.setLayoutY(midVerticalPosition - bomberman.getY());
        } else if (bomberman.getY() < midVerticalPosition) { // set camera upon bomberman position
            canvas.setLayoutY(0);
        } else { // set camera upon bomberman position
            canvas.setLayoutY((HEIGHT - 1 - getRows()) * Sprite.SCALED_SIZE);
        }
    }

    public List<Entity> getStillObjects() {
        return stillObjects;
    }

    public List<Item> getItemGarbage() {
        return itemGarbage;
    }

    public Scene getScene() {
        return scene;
    }

    public List<Brick> getBrickGarbage() {
        return brickGarbage;
    }

    public List<Enemy> getEnemiesGarbage() {
        return enemiesGarbage;
    }

    private void clearGarbage() {
        if (brickGarbage.size() != 0) {
            stillObjects.removeAll(brickGarbage);
            brickGarbage.clear();
        }
        if (enemiesGarbage.size() != 0) {
            enemies.removeAll(enemiesGarbage);
            enemiesGarbage.clear();
        }
        if (itemGarbage.size() != 0) {
            stillObjects.removeAll(itemGarbage);
            itemGarbage.clear();
        }
    }

    public Group getRoot() {
        return root;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public AudioManager getBackgroundMusic() {
        return backgroundMusic;
    }

    private void updateGameInfo() {
        level.setText("Level: " + BombermanGame.numStage);
        score.setText("Score: " + Integer.toString(Score.getScore()));
        time.setText(Long.toString(timer.getTimeValue()));

        if (timer.getTimeValue() == 0 && !TIMEUP) {
            TIMEUP = true;
            enemies.replaceAll(enemy -> new Pontan(enemy.getGridX(),
                    enemy.getGridY(), Sprite.pontan_right1.getFxImage(), this));
        }
    }

    private void afterDead() {
        if (bomberman.checkFatalHit() && Bomber.LIVES != 0) {
            backgroundMusic.stop();
        }

//        System.out.println(Bomber.LIVES);
        if (!bomberman.isAlive()) {
            if (Bomber.LIVES != 0) {
                mainStage.close();
                animationTimer.stop();
                BombermanGame.switchScene(WaitViewManager.getGamePlayScene());
//                BombermanGame.switchScene(WaitViewManager.getGameOverScene());
            } else {
                mainStage.close();
                animationTimer.stop();
                reconfigureSettings();
                BombermanGame.switchScene(WaitViewManager.getGameOverScene());

            }
        }
    }
    public void pauseGame () {
        if (keys.isPressed(KeyCode.ESCAPE) && isCanPressPause) {
            pauseViewManager.setShow(!pauseViewManager.isShow());
            isCanPressPause = false;
        }
        if (!keys.isPressed(KeyCode.ESCAPE) && !isCanPressPause) {
            isCanPressPause = true;
        }
    }

    public void reconfigureSettings() {
        BombermanGame.numStage = 1;
        Bomber.LIVES = 3;
        MenuViewManager.updateLeaderboard();
//        MenuViewManager.playMenuMusic();
    }
}