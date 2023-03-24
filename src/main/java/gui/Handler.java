package gui;


import bots.CFG.CFG;

import bots.TemplateSkills.TemplateSkills;
import db.Conversationdb;


import gui.utils.BotLabel;
import gui.utils.HumanLabel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;



public class Handler implements Initializable {


    public static final String mainTitle = "Multi Modal Digital Assistant";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        botComboBox.getItems().addAll(typeList);
        botComboBox.setValue(typeList.get(0));
    }

    public enum BotType{
        TemplateSkills,
        CFG
    }

    private Stage stage;
    private Scene scene;
    private Parent root;


    private static BotType currentType;

    private Connection connection = Conversationdb.CreateServer();

    // MAIN MENU

    // CHAT
    @FXML public ScrollPane scrollPane; @FXML private VBox chatBox; @FXML private TextField userInput;

    // EDITOR
    @FXML private TextArea fileTextArea; @FXML private Label editorMessage; @FXML private ProgressBar progressBar;
    @FXML private Button resetButton; @FXML private Button saveBtn;


    // SETTING
    @FXML private ComboBox<String> botComboBox = new ComboBox<>();


    /**
     * METHODS FOR MAIN MENU
     */

    public void goToMainMenu(ActionEvent ae) {
        try{
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/scenes/start-page.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle(mainTitle);
        } catch (IOException e){
            System.out.println("FXML: /scenes/start-page.fxml not found");
        }
    }

    public void goToChatBot(ActionEvent ae) {
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/scenes/chat-page.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle(mainTitle + " - Chat Bot");

        } catch (IOException e){
            System.out.println("FXML: /scenes/chat-page.fxml not found");
        } finally {
            setCurrentType();
        }
    }

    public void goToSkillEditor(ActionEvent ae) {
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/scenes/skill-editor.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle(mainTitle + " - Skill Editor");

        } catch (IOException e){
            System.out.println("FXML: /scenes/skill-editor.fxm not found");
        }
    }

    public void goToSettings(ActionEvent ae){
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/scenes/settings.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle(mainTitle + " - Settings");

        } catch (IOException e){
            System.out.println("FXML: /scenes/settings.fxml not found");
        }
    }

    /**
     * METHODS FOR CHAT
     */

    public void addMessageToChat() {
        String sentence = userInput.getText();
        HumanLabel humanLabel = new HumanLabel(sentence);
        String botString = getBotResponse(sentence);

        BotLabel botLabel = new BotLabel(botString);
        chatBox.getChildren().add(humanLabel);
        chatBox.getChildren().add(botLabel);
        Conversationdb.storeConversation(connection, sentence, botString);
        userInput.clear();
    }

    private String getBotResponse(String sentence) {
        switch (currentType){
            case CFG -> {
                return CFG.interpret(sentence);
            }
            case TemplateSkills -> {
                return TemplateSkills.interpret(sentence);
            }
            default -> {
                return "Error: BotType not set";
            }
        }
    }

    /**
     * METHODS FOR SKILL EDITOR
     */

    public static FileTime lastModifiedTime;
    public static File fileInEditor;

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        // Select only .txt files
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
        );
        // Select Resources folder as initial directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/src/main/resources/texts"));
        File fileToLoad = fileChooser.showOpenDialog(null);
        // Load chosen file
        if(fileToLoad != null){
            updateTextArea(fileToLoad);
        }
        // Set last modified time else scheduleFileChecking() will not work
        lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis());
        // Enable save button
        saveBtn.setDisable(false);
    }

    private void updateTextArea(File fileToLoad) {
        progressBar.setVisible(true);
        Task<String> loadFileTask = fileLoaderTask(fileToLoad);
        progressBar.progressProperty().bind(loadFileTask.progressProperty());
        loadFileTask.run();
    }

    private Task<String> fileLoaderTask(File fileToLoad){
        // Load content of the file with a Task
        Task<String> loadFileTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                BufferedReader reader = new BufferedReader(new FileReader(fileToLoad));
                // Calculate number of lines -> used for progress
                long lineCount;
                try (Stream<String> s = Files.lines(fileToLoad.toPath())) {
                    lineCount = s.count();
                }
                // Load lines in memory
                String line;
                StringBuilder fc = new StringBuilder();
                long linesLoaded = 0;
                while((line = reader.readLine()) != null) {
                    fc.append(line);
                    fc.append("\n");
                    // progress bar updated based on the number of lines loaded
                    updateProgress(++linesLoaded, lineCount);
                }
                return fc.toString();
            }
        };
        // If task is successful load content into text area and set status message
        loadFileTask.setOnSucceeded(workerStateEvent -> {
            try {
                fileTextArea.setText(loadFileTask.get());
                editorMessage.setText("File loaded: " + fileToLoad.getName());
                fileInEditor = fileToLoad;
            } catch (InterruptedException | ExecutionException e) {
                fileTextArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
            }
            // Check changes in the file
            setFileChecking(fileInEditor);
        });
        //If task failed display error message
        loadFileTask.setOnFailed(workerStateEvent -> {
            fileTextArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
            editorMessage.setText("Failed to load file");
        });
        return loadFileTask;
    }

    private void setFileChecking(File file){
        ScheduledService<Boolean> fileChecking = scheduledFileChecker(file);
        System.out.println(fileChecking.getLastValue());
        fileChecking.setOnSucceeded(workerStateEvent -> {
            if(fileChecking.getLastValue()==null) return;
            if(fileChecking.getLastValue()){
                // If changes are detected no need to continue checking
                fileChecking.cancel();
                showResetBnt();
            }
        });
        fileChecking.start();
    }

    private void showResetBnt() {
        resetButton.setVisible(true);
    }

    private ScheduledService<Boolean> scheduledFileChecker(File file){
        ScheduledService<Boolean> scheduledService = new ScheduledService<>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        FileTime lastModifiedAsOfNow = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
                        return lastModifiedAsOfNow.compareTo(lastModifiedTime) < 0;
                    }
                };
            }
        };
        // set the time step for each check
        scheduledService.setPeriod(Duration.seconds(5));
        return scheduledService;
    }

    public void resetChanges(){
        updateTextArea(fileInEditor);
        resetButton.setVisible(false);
    }

    public void saveFile() {
        try {
            FileWriter fw = new FileWriter(fileInEditor);
            fw.write(fileTextArea.getText());
            fw.close();
            lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis());
            setMessageSaved();
        } catch (IOException e) {
            System.out.println("Error in saving file");
        }
    }

    private void setMessageSaved(){
        editorMessage.setText("File : '" + fileInEditor.getName() +
                "' saved at: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    /**
     * METHODS FOR SETTINGS
     */

    public static final ObservableList<String> typeList = FXCollections.observableArrayList(typeNames());

    private static String[] typeNames(){
        String[] s = new String[BotType.values().length];
        for (int i = 0; i < BotType.values().length; i++) {
            s[i] = BotType.values()[i].name();
        }
        return s;
    }

    private void setCurrentType(){
        switch (botComboBox.getValue()){
            case "CFG" -> currentType = BotType.CFG;
            case "TemplateSkills" -> currentType = BotType.TemplateSkills;
            default -> currentType = BotType.TemplateSkills;
        }
        System.out.println("Current bot type: " + currentType);
    }

    public void closeApp(){
        System.exit(0);
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().getHour()+
        ":"+LocalDateTime.now().getMinute());
    }

}
