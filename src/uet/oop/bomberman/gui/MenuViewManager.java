package uet.oop.bomberman.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.audio.AudioManager;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.info.Score;
import uet.oop.bomberman.model.GameButton;
import uet.oop.bomberman.model.GameSubScene;
import uet.oop.bomberman.model.InfoLabel;
import uet.oop.bomberman.model.OptionController;

import java.util.ArrayList;
import java.util.List;

public class MenuViewManager {
    private AnchorPane menuPane;
    private static Scene menuScene;
    private Stage menuStage;

    public static AudioManager menuSong =
            new AudioManager("res/audio/menu_song.mp3", AudioManager.BACKGROUND_MUSIC);

    private final static int MENU_BUTTON_START_X = 30;
    private final static int MENU_BUTTON_START_Y = 40;

    List<Button> menuButtons;
    private GameSubScene startSubScene;
    private GameSubScene creditsSubScene;
    private GameSubScene helpSubScene;
    private GameSubScene optionSubScene;
    private GameSubScene scoreSubScene;
    private OptionController backgroundMusic;
    private OptionController gameplayMusic;
    private static InfoLabel score1;
    private static InfoLabel score2;
    private static InfoLabel score3;

    private GameSubScene sceneToHide;

    public MenuViewManager() {
        Score.readScoreListFile();
        menuButtons = new ArrayList<Button>();
        menuPane = new AnchorPane();
        menuScene = new Scene(menuPane, Sprite.SCALED_SIZE * GameViewManager.WIDTH,
                                Sprite.SCALED_SIZE * GameViewManager.HEIGHT);
        menuStage = new Stage();
        menuStage.setScene(menuScene);
        Score.resetScore();
        createBackground();
        createLogo();
        createSubScene();
        createButton();
        playMenuMusic();
    }

    public Stage getMenuStage() {
        return menuStage;
    }

    public Scene getMenuScene() {
        return menuScene;
    }

    // phải truyền được hàm này vào trong handle của OptionController
    public static void playMenuMusic() {
        if (AudioManager.isSoundEnabled(AudioManager.BACKGROUND_MUSIC)) {
            menuSong.play(MediaPlayer.INDEFINITE);
        } else {
            menuSong.pause();
        }
    }

    private void addMenuButton(Button button, int index) {
        button.setLayoutX(MENU_BUTTON_START_X);
        button.setLayoutY(MENU_BUTTON_START_Y + index * 70);
        menuButtons.add(button);
        menuPane.getChildren().add(button);
    }

    private void createButton() {
        createStartButton();
        createScoreButton();
        createHelpButton();
        createOptionButton();
        createCreditsButton();
        createExitButton();
    }

    private void showSubScene(GameSubScene subScene) {
        if (sceneToHide != null) {
            sceneToHide.moveSubScene();
        }

        subScene.moveSubScene();
        sceneToHide = subScene;
    }

    private void createSubScene() {
        startSubScene = new GameSubScene();
        menuPane.getChildren().add(startSubScene);
        startSubScene.getPane().getChildren().add(createMapChooser());

        creditsSubScene = new GameSubScene();
        menuPane.getChildren().add(creditsSubScene);
        creditsSubScene.getPane().getChildren().add(createCreditsPane());

        helpSubScene = new GameSubScene();
        menuPane.getChildren().add(helpSubScene);
        helpSubScene.getPane().getChildren().add(createInstruction());

        optionSubScene = new GameSubScene();
        menuPane.getChildren().add(optionSubScene);
        optionSubScene.getPane().getChildren().add(createOptions());

        scoreSubScene = new GameSubScene();
        menuPane.getChildren().add(scoreSubScene);
        scoreSubScene.getPane().getChildren().add(createLeaderboard());
    }

    private void createStartButton() {
        GameButton startButton = new GameButton("PLAY GAME");
        addMenuButton(startButton, 0);
//        menu = this;

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /*GameViewManager gameView = new GameViewManager(BombermanGame.numStage);
                gameView.createNewGame();
                menuStage.close();
                BombermanGame.switchScene(gameView.getScene());*/
//                WaitViewManager waitView = new WaitViewManager();
//                menuSong.stop();
//                BombermanGame.switchScene(waitView.getWaitScene());
                showSubScene(startSubScene);
            }
        });
    }

    private AnchorPane createMapChooser() {
        AnchorPane pane = new AnchorPane();

        BackgroundImage backgroundLabel =
                new BackgroundImage(new Image("/model/img/red_info_label.png", 250, 35, false, true),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        InfoLabel startGame = new InfoLabel("START GAME");
        startGame.setLayoutX(37);
        startGame.setLayoutY(15);
        startGame.setFont("res/model/font/PixelEmulator-xq08.ttf");
        startGame.setBackground(new Background(backgroundLabel));
        startGame.setAlignment(Pos.CENTER);

        GameButton newGameButton = new GameButton("New Game");
        newGameButton.setLayoutX(65);
        newGameButton.setLayoutY(80);
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                WaitViewManager waitView = new WaitViewManager();
                menuSong.stop();
                BombermanGame.switchScene(waitView.getGamePlayScene());
            }
        });

        VBox box = new VBox();
        box.setSpacing(5);
        InfoLabel levelChooser = new InfoLabel("Choose a level");
        levelChooser.setAlignment(Pos.CENTER);
        levelChooser.setLayoutX(20);
        levelChooser.setLayoutY(20);
        FlowPane flowPane = new FlowPane(10,10);
        Button[] levelButton = new Button[BombermanGame.maxStage];
        for (int i = 0; i < BombermanGame.maxStage; i++) {
            Label label = new Label();
            label.setText(Integer.toString(i + 1));
            levelButton[i] = new Button(Integer.toString(i + 1));
            levelButton[i].setFont(Font.font("MV Boli", 20));
            int finalI = i;
            levelButton[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    BombermanGame.numStage = finalI + 1;
                    WaitViewManager waitView = new WaitViewManager();
                    menuSong.stop();
                    BombermanGame.switchScene(waitView.getGamePlayScene());
                }
            });
            flowPane.getChildren().add(levelButton[i]);
        }

        flowPane.setPrefWrapLength(200);
        flowPane.setPrefWidth(200);
        flowPane.setPrefHeight(100);
        flowPane.setAlignment(Pos.TOP_CENTER);

        box.setLayoutX(35);
        box.setLayoutY(150);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(levelChooser, flowPane);

        pane.getChildren().addAll(startGame, newGameButton, box);
        return pane;
    }

    private void createScoreButton() {
        GameButton scoreButton = new GameButton("SCORES");
        addMenuButton(scoreButton, 1);

        scoreButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(scoreSubScene);
            }
        });
    }

    private AnchorPane createLeaderboard() {
        AnchorPane pane = new AnchorPane();
        
        BackgroundImage backgroundLabel =
                new BackgroundImage(new Image("/model/img/red_info_label.png", 250, 35, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        InfoLabel leaderboard = new InfoLabel("LEADERBOARD");
        leaderboard.setLayoutX(37);
        leaderboard.setLayoutY(15);
        leaderboard.setFont("res/model/font/PixelEmulator-xq08.ttf");
        leaderboard.setBackground(new Background(backgroundLabel));
        leaderboard.setAlignment(Pos.CENTER);

        HBox top1 = new HBox();
        top1.setSpacing(30);
        top1.setAlignment(Pos.CENTER);
        HBox top2 = new HBox();
        top2.setSpacing(30);
        top2.setAlignment(Pos.CENTER);
        HBox top3 = new HBox();
        top3.setSpacing(30);
        top3.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        ImageView goldMedal = new ImageView(new Image("/model/img/gold_medal.png"));
        ImageView silverMedal = new ImageView(new Image("/model/img/silver_medal.png"));
        ImageView bronzeMedal = new ImageView(new Image("/model/img/bronze_medal.png"));
        score1 = new InfoLabel(Integer.toString(Score.scoreList.get(0)));
        score2 = new InfoLabel(Integer.toString(Score.scoreList.get(1)));
        score3 = new InfoLabel(Integer.toString(Score.scoreList.get(2)));
        score1.setFont("res/model/font/PixelEmulator-xq08.ttf");
//        score1.setPadding(new Insets(10, 10, 10, 10));
        score2.setFont("res/model/font/PixelEmulator-xq08.ttf");
//        score2.setPadding(new Insets(10, 10, 10, 10));
        score3.setFont("res/model/font/PixelEmulator-xq08.ttf");
//        score3.setPadding(new Insets(10, 10, 10, 10));
        top1.getChildren().addAll(goldMedal, score1);
        top2.getChildren().addAll(silverMedal, score2);
        top3.getChildren().addAll(bronzeMedal, score3);

        vbox.getChildren().add(top1);
        vbox.getChildren().add(top2);
        vbox.getChildren().add(top3);
        vbox.setLayoutX(70);
        vbox.setLayoutY(70);

        pane.getChildren().add(leaderboard);
        pane.getChildren().add(vbox);
        return pane;
    }

    public static void updateLeaderboard() {
        Score.updateTopScore();
        Score.readScoreListFile();
        score1.setText(Integer.toString(Score.scoreList.get(0)));
        score2.setText(Integer.toString(Score.scoreList.get(1)));
        score3.setText(Integer.toString(Score.scoreList.get(2)));
    }

    private void createHelpButton() {
        GameButton helpButton = new GameButton("HELP");
        addMenuButton(helpButton, 2);

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(helpSubScene);
            }
        });
    }

    private AnchorPane createInstruction() {
        AnchorPane pane = new AnchorPane();
        // create label
        BackgroundImage backgroundLabel =
                new BackgroundImage(new Image("/model/img/red_info_label.png", 250, 35, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        InfoLabel instruction = new InfoLabel("HOW TO PLAY");
        instruction.setLayoutX(37);
        instruction.setLayoutY(15);
        instruction.setFont("res/model/font/PixelEmulator-xq08.ttf");
        instruction.setBackground(new Background(backgroundLabel));
        instruction.setAlignment(Pos.CENTER);

        // create arrows instruction
        HBox box1 = new HBox();
        box1.setSpacing(20);
        box1.setAlignment(Pos.CENTER);
        ImageView arrows = new ImageView(new Image("/model/img/arrows.png"));
        InfoLabel control = new InfoLabel("CONTROL");
        box1.getChildren().addAll(arrows, control);

        // create space bar instruction
        HBox box2 = new HBox();
        box2.setSpacing(20);
        box2.setAlignment(Pos.CENTER);
        ImageView spacebar = new ImageView(new Image("/model/img/spacebar.png"));
        InfoLabel setBomb = new InfoLabel("PLACE BOMB");
        box2.getChildren().addAll(spacebar, setBomb);

        // create esc key instruction
        HBox box3 = new HBox();
        box3.setSpacing(20);
        box3.setAlignment(Pos.CENTER);
        ImageView escKey = new ImageView(new Image("/model/img/escape.png"));
        InfoLabel gamePause = new InfoLabel("PAUSE GAME");
        box3.getChildren().addAll(escKey, gamePause);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(box1, box2, box3);
        vbox.setLayoutX(50);
        vbox.setLayoutY(70);

        pane.getChildren().add(instruction);
        pane.getChildren().add(vbox);
        return pane;
    }

    private void createOptionButton() {
        GameButton optionButton = new GameButton("OPTION");
        addMenuButton(optionButton, 3);

        optionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(optionSubScene);
            }
        });
    }

    private AnchorPane createOptions() {
        AnchorPane pane = new AnchorPane();
        BackgroundImage backgroundLabel =
                new BackgroundImage(new Image("/model/img/red_info_label.png", 250, 35, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        InfoLabel setting = new InfoLabel("SETTING");
        setting.setLayoutX(37);
        setting.setLayoutY(15);
        setting.setFont("res/model/font/PixelEmulator-xq08.ttf");
        setting.setBackground(new Background(backgroundLabel));
        setting.setAlignment(Pos.CENTER);

        VBox box = new VBox();
        box.setSpacing(5);
//        box.setAlignment(Pos.CENTER);
        InfoLabel label1 = new InfoLabel("Background Music: ");
        backgroundMusic = new OptionController(AudioManager.BACKGROUND_MUSIC);
        backgroundMusic.changeVolume(menuSong);
        InfoLabel label2 = new InfoLabel("Gameplay Music: ");
        gameplayMusic = new OptionController(AudioManager.GAMEPLAY_MUSIC);
        gameplayMusic.changeVolume(AudioManager.GAMEPLAY_MUSIC);

        box.getChildren().add(label1);
        box.getChildren().add(backgroundMusic);
        box.getChildren().add(label2);
        box.getChildren().add(gameplayMusic);
//        box.getChildren().add(setLevelLabel);
//        box.getChildren().add(inputLevel);
//        box.getChildren().add(submitButton);
//        box.getChildren().add(notification);
//        box.setAlignment(Pos.CENTER);
        box.setLayoutX(70);
        box.setLayoutY(70);
        pane.getChildren().add(setting);
        pane.getChildren().add(box);
        return pane;
    }

    private void createCreditsButton() {
        GameButton creditsButton = new GameButton("CREDITS");
        addMenuButton(creditsButton, 4);

        creditsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showSubScene(creditsSubScene);
            }
        });
    }

    private AnchorPane createCreditsPane() {
        AnchorPane pane = new AnchorPane();
        BackgroundImage backgroundLabel =
                new BackgroundImage(new Image("/model/img/red_info_label.png", 250, 35, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        InfoLabel credits= new InfoLabel("CREDITS");
        credits.setLayoutX(37);
        credits.setLayoutY(15);
        credits.setFont("res/model/font/PixelEmulator-xq08.ttf");
        credits.setBackground(new Background(backgroundLabel));
        credits.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER);
        InfoLabel label = new InfoLabel("A game created and developed by Le Trong Minh\n & Nguyen Hoai Nam");
        label.setPrefWidth(270);
        label.setPrefHeight(70);
        label.setTextAlignment(TextAlignment.CENTER);
        ImageView bombermanIcon = new ImageView(new Image("/model/img/bomberman2.png"));
        vbox.getChildren().addAll(label, bombermanIcon);
        vbox.setLayoutX(25);
        vbox.setLayoutY(70);

        pane.getChildren().add(credits);
        pane.getChildren().add(vbox);
        return pane;
    }

    private void createExitButton() {
        GameButton exitButton = new GameButton("EXIT");
        addMenuButton(exitButton, 5);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    private void createLogo() {
        ImageView logo = new ImageView("/model/img/logo.png");
        logo.setLayoutX(280);
        logo.setLayoutY(5);

        logo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logo.setEffect(new DropShadow());
            }
        });

        logo.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logo.setEffect(null);
            }
        });

        menuPane.getChildren().add(logo);
    }

    private void createBackground() {
        Image backgroundImage = new Image("/model/img/background.png", 800, 600, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, null,
                        null, BackgroundPosition.DEFAULT, null);
        menuPane.setBackground(new Background(background));
    }

    public static Scene getScene() {
        return menuScene;
    }
}
