package uet.oop.bomberman.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class InfoLabel extends Label {

    public InfoLabel(String text) {
        setPrefWidth(300);
        setPrefHeight(30);
        setPadding(new Insets(10, 10, 10, 10));
        setText(text);
        setWrapText(true);
        setFont(new Font("Verdana", 15));
    }
}
