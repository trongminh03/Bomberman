package uet.oop.bomberman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import uet.oop.bomberman.constants.GlobalConstants;
import uet.oop.bomberman.gui.MenuViewManager;

public class BombermanGame extends Application {
    private static Stage stage;
    public static int numStage = 1;
    public final static int maxStage = 2;
    private MenuViewManager menuView;
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        menuView = new MenuViewManager();
        stage.setScene(menuView.getMenuScene());
        stage.getIcons().add(new Image("model/img/gameicon.png"));
//        stage.setResizable(false);
        stage.setTitle(GlobalConstants.GAME_NAME + GlobalConstants.GAME_VERSION);
        stage.show();
    }

    public static void switchScene(Scene scene) {
        stage.setScene(scene);
    }

}
