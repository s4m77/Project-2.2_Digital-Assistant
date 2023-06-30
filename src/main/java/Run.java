import gui.ChatApplication;
import gui.utils.PyCaller;

/**
 * MAIN RUNNER FOR THE APPLICATION
 */

public class Run {
    public static void main(String[] args) {
        PyCaller.startServer();
        ChatApplication.main(args);
    }
}
