package uet.oop.bomberman.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.info.Score;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

enum TypeButton {
    HOME, RESET
}

class ImgButton extends Button {
    private final String HOME_BUTTON
            = "-fx-background-color: transparent; -fx-background-image: url('/model/home_button.png');";
    private final String RESET_BUTTON
            = "-fx-background-color: transparent; -fx-background-image: url('/model/reset_button.png');";
    private final int BUTTON_WIDTH = 64;
    private final int BUTTON_HEIGHT = 64;
    TypeButton type;
    public ImgButton(TypeButton type) {
        this.type = type;
        setPrefWidth(BUTTON_WIDTH);
        setPrefHeight(BUTTON_HEIGHT);
        if (this.type == TypeButton.HOME) {
            setStyle(HOME_BUTTON);
        } else if (this.type == TypeButton.RESET) {
            setStyle(RESET_BUTTON);
        }
        initializeButtonListener();
    }

    public void initializeButtonListener() {
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(new DropShadow());
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setEffect(null);
            }
        });
    }
}

public class PauseViewManager extends SubScene {
    private final static String BACKGROUND_IMAGE = "/model/img/background.png";
    private String fontPath = "res/model/font/PixelEmulator-xq08.ttf";
    private ImgButton homeButton;
    private ImgButton resetButton;
    private Label message;
    private Label infor;
    private HBox buttonBox;
    private boolean isShow;
    GameViewManager game;
    public PauseViewManager(GameViewManager game) {
        super(new AnchorPane(), 250, 150);
        this.game = game;
        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, 250, 150, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        isShow = false;
        initLabel();
        createButtonReset();
        createButtonHome();
        buttonBox = new HBox();
        createButton();
        AnchorPane root = (AnchorPane) this.getRoot();
        root.setBackground(new Background(image));
        root.getChildren().add(buttonBox);
        root.getChildren().add(infor);
        root.getChildren().add(message);
        setLayoutX(200);
        setLayoutY(170);
    }


    private void initLabel() {
        infor = new Label("PAUSE");
        infor.setLayoutX(100);
        infor.setLayoutY(0);
        message = new Label("Press ESC to continue");
        message.setLayoutX(30);
        message.setLayoutY(130);
        try {
            message.setFont(Font.loadFont(new FileInputStream(fontPath), 12));
        } catch (FileNotFoundException e) {
            message.setFont(new Font("Verdana", 10));
        }
        try {
            infor.setFont(Font.loadFont(new FileInputStream(fontPath), 18));
        } catch (FileNotFoundException e) {
            infor.setFont(new Font("Verdana", 20));
        }
    }

    private void createButton() {
        createButtonHome();
        createButtonReset();
        buttonBox.getChildren().add(resetButton);
        buttonBox.getChildren().add(homeButton);
        buttonBox.setSpacing(50);
        buttonBox.setLayoutX(35);
        buttonBox.setLayoutY(40);
    }

    private void createButtonReset() {
        resetButton = new ImgButton(TypeButton.RESET);
        resetButton.setLayoutX(0);
        resetButton.setLayoutY(30);
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                game.getBackgroundMusic().stop();
                Bomber.LIVES = 3;
                Score.resetScore();
                BombermanGame.switchScene(WaitViewManager.getWaitScene());
                System.out.println("reset");
            }
        });

    }

    private void createButtonHome() {
        homeButton = new ImgButton(TypeButton.HOME);
        homeButton.setLayoutX(50);
        homeButton.setLayoutY(30);
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                setShow(false);
                game.reconfigureSettings();
                game.getBackgroundMusic().stop();
                BombermanGame.switchScene(MenuViewManager.getScene());
                System.out.println("home");
            }
        });
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
