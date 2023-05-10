package gui.utils.messages;

import gui.utils.messages.ALabel;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;

public class HumanLabel extends ALabel {
    public HumanLabel(String text) {
        super();
        this.setText(text + " <- YOU");
        this.setStyle("-fx-background-color:#E6E6FA");
        this.setAccessibleText(text);
        this.setStyle("""
                    -fx-background-color: #E6E6FA;
                    -fx-font-family: Arial;
                    -fx-font-size: 14px;
                    -fx-max-width: 500px;
                    -fx-text-fill: black;
                    -fx-background-radius: 20;
                    -fx-padding: 10px;
                    -fx-wrap-text: true;
                    -fx-alignment:CENTER-RIGHT;\
                """);
        this.setAlignment(Pos.BASELINE_RIGHT);
        this.setTextAlignment(TextAlignment.JUSTIFY);
        this.setWrapText(true);

    }
}
