package uet.oop.bomberman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uet.oop.bomberman.constants.GlobalConstants;
import uet.oop.bomberman.gui.MenuViewManager;
import uet.oop.bomberman.gui.WaitViewManager;

public class BombermanGame extends Application {
    private static Stage stage;
    public static int numStage = 2;
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        MenuViewManager menuView = new MenuViewManager();
        stage.setScene(menuView.getMenuScene());
        stage.setTitle(GlobalConstants.GAME_NAME + GlobalConstants.GAME_VERSION);
        stage.show();

        /*this.stage = primaryStage;
        WaitViewManager waitView = new WaitViewManager();
        stage.setScene(waitView.getWaitScene());
        stage.show();*/
    }

    public static void switchScene(Scene scene) {
        stage.setScene(scene);
    }


}
