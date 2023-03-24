package gui.utils;

import javafx.scene.control.Label;

/**
 * Abstract class for intended to set default properties for labels
 */
public abstract class ALabel extends Label {
    public ALabel() {
        super();
        this.setPrefSize(570, 20);
        this.setWrapText(true);
    }
}
