package uet.oop.bomberman.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import uet.oop.bomberman.BombermanGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public PauseViewManager() {
        super(new AnchorPane(), 250, 150);
        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, 250, 150, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        isShow = false;
        initLabel();
        creatButtonReset();
        creatButtonHome();
        buttonBox = new HBox();
        creatButton();
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

    private void creatButton() {
        creatButtonHome();
        creatButtonReset();
        buttonBox.getChildren().add(resetButton);
        buttonBox.getChildren().add(homeButton);
        buttonBox.setSpacing(50);
        buttonBox.setLayoutX(35);
        buttonBox.setLayoutY(40);
    }

    private void creatButtonReset() {
        resetButton = new ImgButton(TypeButton.RESET);
        resetButton.setLayoutX(0);
        resetButton.setLayoutY(30);
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                BombermanGame.switchScene(WaitViewManager.getWaitScene());
                System.out.println("reset");
            }
        });

    }

    private void creatButtonHome() {
        homeButton = new ImgButton(TypeButton.HOME);
        homeButton.setLayoutX(50);
        homeButton.setLayoutY(30);
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                setShow(false);
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
