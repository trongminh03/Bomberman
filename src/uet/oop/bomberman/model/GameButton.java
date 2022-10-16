package uet.oop.bomberman.model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameButton extends Button {
    private final static String mainFont = "res/model/PixelEmulator-xq08.ttf";
    private final String BUTTON_FREE_STYLE
            = "-fx-background-color: transparent; -fx-background-image: url('/model/yellow_button.png');";
    private final String BUTTON_PRESSED_STYLE
            = "-fx-background-color: transparent; -fx-background-image: url('/model/yellow_button_pressed.png');";
    private final int BUTTON_WIDTH = 190;
    private final int BUTTON_HEIGHT = 49;

    public GameButton(String text) {
        setText(text);
//        setFont(Font.font("Verdana", 23));
        setMainFont();
        setPrefWidth(BUTTON_WIDTH);
        setPrefHeight(BUTTON_HEIGHT);
        setStyle(BUTTON_FREE_STYLE);
        initializeButtonListener();
    }

    public void setMainFont() {
        try {
            setFont(Font.loadFont(new FileInputStream(mainFont), 20));
        } catch (FileNotFoundException e) {
            System.out.println("Cannot load font");
        }
    }

    public void setButtonReleasedStyle() {
        setStyle(BUTTON_FREE_STYLE);
        setPrefHeight(BUTTON_HEIGHT);
        setLayoutY(getLayoutY() - 4);
    }

    public void setButtonPressedStyle() {
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(BUTTON_HEIGHT - 4);
        setLayoutY(getLayoutY() + 4);
    }

    public void initializeButtonListener() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonPressedStyle();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    setButtonReleasedStyle();
                }
            }
        });

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
