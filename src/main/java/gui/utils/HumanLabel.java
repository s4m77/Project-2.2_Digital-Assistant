package gui.utils;

import javafx.geometry.Pos;

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
                    -fx-text-fill: black;
                    -fx-background-radius: 20;
                    -fx-max-width: 500px;
                    -fx-padding: 10px;
                    -fx-alignment:CENTER-RIGHT;\
                """);
        this.setAlignment(Pos.BASELINE_RIGHT);
    }
}
