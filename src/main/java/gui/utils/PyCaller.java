package gui.utils;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLOutput;

/**
 * This class is used to call Python scripts from Java.
 */
public class PyCaller {

    static final String ENV_DIR = System.getProperty("user.dir") + "/mmda_venv";

    static final String COMMAND = "mmda_venv/bin/python ";
    static final String SCRIPT = System.getProperty("user.dir")+"/src/main/java/gui/utils/Test.py ";
    static final String METHOD = "test";


    public static void main(String[] args) throws IOException {

        String cmd = "mmda_venv/bin/python src/main/java/FacialRecognision/Model.py";
        System.out.println(executeCommand(cmd));

    }

    private static String executeCommand(String command){
        try{
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.environment().put("VIRTUAL_ENV", ENV_DIR);
            processBuilder.redirectErrorStream(false); // set true for debugging (will print python errors)
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                output.append(line).append("\n");
            }
            // wait for python response
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Command execution failed");
            }
            return output.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
