package gui.utils.messages;

import javafx.scene.control.Label;

/**
 * Abstract class for intended to set default properties for labels
 */
public abstract class ALabel extends Label {
    public ALabel() {
        super();
        this.setWrapText(true);
    }
}
