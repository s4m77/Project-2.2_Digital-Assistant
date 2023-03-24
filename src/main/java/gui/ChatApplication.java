package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("/gui/scenes/start-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(Handler.MAIN_TITLE);
        stage.setScene(scene);
        stage.show();
    }
}
