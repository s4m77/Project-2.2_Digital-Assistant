package gui;

import gui.utils.Handler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ChatApplication extends Application {

    private final URL loginScene = getClass().getResource("/gui/scenes/login.fxml");

    @Override
    public void start(Stage stage) throws Exception {
        //FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("/gui/scenes/login.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(loginScene);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(Handler.MAIN_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ChatApplication.launch(args);
    }
}
