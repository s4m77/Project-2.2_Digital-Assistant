package Calculator;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class MyButton extends Button {
    public MyButton(String text) {
        super(text);
        setPrefWidth(50);
        setPrefHeight(50);
        setFont(new Font("Verdana", 20));
    }
}
