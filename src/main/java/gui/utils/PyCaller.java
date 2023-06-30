package gui.utils;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to call Python scripts from Java.
 */
public class PyCaller {

    static final String ENV_DIR = System.getProperty("user.dir") + "/mmda_venv";

    static final String COMMAND = "mmda_venv/bin/python ";
    static final String MODEL_SCRIPT = System.getProperty("user.dir")+"/src/main/java/FacialRecognition/CNNModel.py ";


    public static void main(String[] args) throws IOException {

//        startServer();
//        System.out.println("Server started");
//        System.out.println(predictAction("is 7 + 3 equal to 10"));
//        System.out.println(findUser());
        System.out.println(findUser());
    }

    public static void startServer(){
        executeCommand("mmda_venv/bin/python src/main/resources/mmda_nlp/infer.py");
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
        return filter(out);
    }

    public static String filter(String input) {
        String lastValue = null;
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            lastValue = matcher.group(1);
        }
        assert lastValue != null;
        return lastValue.substring(1, lastValue.length() - 1);
    }

}
