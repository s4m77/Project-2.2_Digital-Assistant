package gui;


import CFG.CFG;

import TemplateSkills.TemplateSkills;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Handler implements Initializable {



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


    public static BotType currentType = BotType.TemplateSkills;

    private Connection connection = Conversationdb.CreateServer();

    // MAIN MENU
    @FXML private Button chatBotButton; @FXML private Button skillEditorButton; @FXML private Label welcomeLabel;

    // CHAT
    @FXML private VBox chatBox; @FXML private TextField userInput;

    // EDITOR
    @FXML private TextArea fileTextArea; @FXML private Label message; @FXML private ProgressBar progressBar; @FXML private Button resetButton;

    // SETTING
    @FXML private ComboBox<String> botComboBox = new ComboBox<>();


    /**
     * METHODS FOR MAIN MENU
     */

    public void goToMainMenu(ActionEvent ae) {
        try{
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/start-page.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println("FXML: /scenes/start-page.fxml not found");
        }
    }

    public void goToChatBot(ActionEvent ae) {
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/chat-page.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            currentType = BotType.valueOf(botComboBox.getValue());
            System.out.println("Current bot type: " + currentType);
        } catch (IOException e){
            System.out.println("FXML: /scenes/chat-page.fxml not found");
        }
    }

    public void goToSkillEditor(ActionEvent ae) {
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/skill-editor.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println("FXML: /scenes/skill-editor.fxm not found");
        }
    }

    public void goToSettings(ActionEvent ae){
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/settings.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

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
        String botString = "";
        switch (currentType){
            case CFG -> {
                botString = CFG.interpret(sentence);
            }
            case TemplateSkills -> {
                botString = TemplateSkills.interpret(sentence);
            }
        }
        return botString;
    }

    /**
     * METHODS FOR SKILL EDITOR
     */

    public static FileTime lastModifiedTime;
    public static File loadedFileReference;

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        //txtFileManager = new TXTFileManager(fileTextArea, message, resetButton, progressBar);
        // Select only .txt files
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
        );
        // Select Resources folder as initial directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/src/main/resources/txts"));
        File fileToLoad = fileChooser.showOpenDialog(null);
        // Load chosen file
        if(fileToLoad != null){
            updateTextArea(fileToLoad);
        }
        // Set last modified time else scheduleFileChecking() will not work
        lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis());
    }

    private void updateTextArea(File fileToLoad) {
        Task<String> loadFileTask = fileLoaderTask(fileToLoad);
        progressBar.progressProperty().bind(loadFileTask.progressProperty());
        loadFileTask.run();
    }

    private Task<String> fileLoaderTask(File fileToLoad){
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
                    updateProgress(++linesLoaded, lineCount);
                }
                return fc.toString();
            }
        };
        // If task is successful load content into text area and set status message
        loadFileTask.setOnSucceeded(workerStateEvent -> {
            try {
                fileTextArea.setText(loadFileTask.get());
                message.setText("File loaded: " + fileToLoad.getName());
                loadedFileReference = fileToLoad;
            } catch (InterruptedException | ExecutionException e) {
                fileTextArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
            }
            // Check changes in the file
            scheduleFileChecking(loadedFileReference);
        });
        //If task failed display error message
        loadFileTask.setOnFailed(workerStateEvent -> {
            fileTextArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
            message.setText("Failed to load file");
        });
        return loadFileTask;
    }

    private void scheduleFileChecking(File file){
        ScheduledService<Boolean> fileChangeCheck = createFileChangesChecker(file);
        System.out.println(fileChangeCheck.getLastValue());
        fileChangeCheck.setOnSucceeded(workerStateEvent -> {
            if(fileChangeCheck.getLastValue()==null) return;
            if(fileChangeCheck.getLastValue()){
                // stop checking for changes if changes are detected
                fileChangeCheck.cancel();
                showResetButton();
            }
        });
        System.out.println("Checking for changes has started");
        fileChangeCheck.start();
    }

    private void showResetButton() {
        resetButton.setVisible(true);
    }

    private ScheduledService<Boolean> createFileChangesChecker(File file){
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
        updateTextArea(loadedFileReference);
        resetButton.setVisible(false);
    }

    public void saveFile() {
        try {
            FileWriter fw = new FileWriter(loadedFileReference);
            fw.write(fileTextArea.getText());
            fw.close();
            lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis());
            System.out.println(lastModifiedTime);
            System.out.println("File update successful");
        } catch (IOException e) {
            System.out.println("Error in saving file");
        }
    }

    /**
     * METHODS FOR SETTINGS
     */

    private static final ObservableList<String> typeList = FXCollections.observableArrayList(typeNames());

    private static String[] typeNames(){
        String[] s = new String[BotType.values().length];
        for (int i = 0; i < BotType.values().length; i++) {
            s[i] = BotType.values()[i].name();
        }
        return s;
    }

    private void setCurrentType(){}

    public void closeApp(){
        System.exit(0);
    }


}
