package Calculator;

import javax.swing.*;

public class MyButton extends JButton {
    public MyButton(String text, int x, int y, int width, int height){
        super(text);
        setBounds(x, y, width, height);
    }
}
