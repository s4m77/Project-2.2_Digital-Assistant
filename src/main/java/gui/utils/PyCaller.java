package gui.utils;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is used to call Python scripts from Java.
 */
public class PyCaller {

    static final boolean WINDOWS = System.getProperty("os.name").toLowerCase().startsWith("windows");
    static final String ENV_DIR = System.getProperty("user.dir") + "/mmda_venv";
    static final String REQ_DIR = System.getProperty("user.dir") + "/src/main/resources/py/requirements.txt";
    static final boolean SETUP = !Files.exists(Paths.get(System.getProperty("user.dir") + "/mmda_venv"));

    static final String COMMAND = "mmda_venv/bin/python ";
    static final String SCRIPT = System.getProperty("user.dir")+"/src/main/java/gui/utils/Test.py ";
    static final String METHOD = "test";


    public static void main(String[] args) throws IOException {

        setPyEnv();
        String cmd = COMMAND + SCRIPT + METHOD;
        System.out.println(executeCommand(cmd));;

    }

    private static void setPyEnv(){
        // Create the venv
        String createCommand = "python -m venv " + ENV_DIR;
        executeCommand(createCommand);
        System.out.println("Virtual environment created successfully.");

        // Activate
        String activateCommand = getActivateCommand();;
        activateCommand = "source " + ENV_DIR + "/bin/activate";
        executeCommand(activateCommand);

        // Install requirements
        String installCommand = "pip install -r " + REQ_DIR;
        executeCommand(installCommand);
        System.out.println("Virtual environment activated successfully.");
    }

    private static String getActivateCommand() {
        return WINDOWS ? "cmd /c" + ENV_DIR + "mmda_venv/Scripts/activate.bat" : //not sure for windows
                "source " + System.getProperty("user.dir")+ "/mmda_venv/bin/activate";
    }

    private static String executeCommand(String command){
        try{
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.environment().put("VIRTUAL_ENV", ENV_DIR);
            processBuilder.redirectErrorStream(true);
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
