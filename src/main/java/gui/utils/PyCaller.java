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
    static final String MODEL_SCRIPT = System.getProperty("user.dir")+"/src/main/java/gui/utils/Test.py ";


    public static void main(String[] args) throws IOException {

//        String cmd = COMMAND + SCRIPT + METHOD;
//        System.out.println(executeCommand(cmd));;
        String cmd = "mmda_venv/bin/python src/main/java/gui/utils/Test.py test \"hello\" \"world\"";
        cmd = "mmda_venv/bin/python src/main/java/FacialRecognision/Model.py";
        System.out.println(executeCommand(cmd));

        String out = "['Carlos_Moya', 'Barbara_De', ...]";
        // out will be of format ['Carlos_Moya', 'Barbara_De', ...]
        // need to get the name of the first person, e.g. Carlos_Moya
        String[] names = out.split(",");
        if (names.length > 0) {
            String name = names[0];
            name = name.substring(2, name.length() - 1);
            System.out.println(name);
        }

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

    public static String findUser() {
        // mmda_venv/bin/python src/main/java/FacialRecognition/Model.py
        String out = executeCommand(COMMAND + MODEL_SCRIPT);
        // out will be of format ['Carlos_Moya', 'Barbara_De', ...]
        // need to get the name of the first person, e.g. Carlos_Moya
        if (out != null) {
            String[] names = out.split(",");
            if (names.length > 0) {
                String name = names[0];
                name = name.substring(2, name.length() - 1);
                return name;
            }
        }
        return "";
    }

}
