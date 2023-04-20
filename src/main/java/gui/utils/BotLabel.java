package gui.utils;

import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;

public class BotLabel extends ALabel {
    public BotLabel(String text) {
        super();
        this.setText("BOT -> "+text);
        this.setStyle("-fx-background-color:#00FFFF");
        this.setAccessibleText(text);
        this.setStyle("""
                    -fx-background-color: #00FFFF;
                    -fx-background-radius: 20;
                    -fx-max-width: 500px;
                    -fx-padding: 10px;
                    -fx-font-family: Roboto;
                    -fx-font-size: 14px;
                    -fx-text-fill: black;
                    -fx-wrap-text: true;
                    -fx-alignment:CENTER-LEFT;\
                """);
        this.setAlignment(Pos.BASELINE_LEFT);
        this.setTextAlignment(TextAlignment.JUSTIFY);
        this.setWrapText(true);

    }
}
