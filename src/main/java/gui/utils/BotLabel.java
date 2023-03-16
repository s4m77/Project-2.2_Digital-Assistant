package gui.utils;

import javafx.scene.control.Label;

public class BotLabel extends Label {
    public BotLabel(String text) {
        super();
        this.setText("BOT -> "+text);
        this.setStyle("-fx-background-color:#00FFFF");
        this.setPrefSize(570, 20);
    }
}
