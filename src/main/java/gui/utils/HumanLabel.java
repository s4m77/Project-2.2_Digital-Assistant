package gui.utils;

import javafx.scene.control.Label;

public class HumanLabel extends Label {
    public HumanLabel(String text) {
        super();
        this.setText("YOU: "+text);
        this.setStyle("-fx-background-color:#E6E6FA");
    }
}
