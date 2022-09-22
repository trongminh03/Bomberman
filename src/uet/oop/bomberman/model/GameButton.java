package uet.oop.bomberman.model;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class GameButton extends Button {
//    private final String mainFont = "/model/KaushanScript-Regular.otf";
    private final String BUTTON_FREE_STYLE
            = "-fx-background-color: transparent; -fx-background-image: url('/model/yellow_button.png');";
    private final String BUTTON_PRESSED_STYLE
            = "-fx-background-color: transparent; -fx-background-image: url('/model/yellow_button_pressed.png');";
    private final int BUTTON_WIDTH = 190;
    private final int BUTTON_HEIGHT = 49;

    public GameButton(String text) {
        setText(text);
        setFont(Font.font("Verdana", 23));
//        setMainFont();
        setPrefWidth(BUTTON_WIDTH);
        setPrefHeight(BUTTON_HEIGHT);
        setStyle(BUTTON_FREE_STYLE);
        initializeButtonListener();
    }

//    public void setMainFont() {
//        setFont(Font.font(mainFont, 23));
//    }

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
