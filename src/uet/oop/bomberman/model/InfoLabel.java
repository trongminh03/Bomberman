package uet.oop.bomberman.model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InfoLabel extends Label {
    private final static String font = "res/model/font/SVN-Coder's Crux.otf";

    public InfoLabel(String text) {
        setPrefWidth(250);
        setPrefHeight(35);
//        setPadding(new Insets(10, 10, 10, 10));
        setText(text);
        setWrapText(true);
        setFont(font);
    }

    public void setFont(String path) {
        try {
            setFont(Font.loadFont(new FileInputStream(path), 25));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
