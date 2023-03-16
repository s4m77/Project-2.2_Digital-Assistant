package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("/scenes/start-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Multi-Modal Digital Assistant");
        stage.setScene(scene);
        stage.show();
    }
}
