package gui.utils;

import bots.CFG.CFG;
import bots.TemplateSkills.TemplateSkills;
import db.Conversationdb;
import gui.utils.messages.BotLabel;
import gui.utils.messages.HumanLabel;

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
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/**
 * This class is the main controller for the GUI.
 * It handles all the events and actions that are triggered by the user.
 * Path to .fxml Files: /src/main/resources/gui/scenes
 */
public class Handler implements Initializable {

    public static final String MAIN_TITLE = "Multi-Modal Digital Assistant";

    private final Connection connection = Conversationdb.CreateServer();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static BotType currentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        botComboBox.getItems().addAll(typeList);
        if (currentType != null)
            botComboBox.setValue(String.valueOf(currentType));
        else
            botComboBox.setValue(typeList.get(0));
        setCurrentType();
        userApp = new UserApp(this.connection);

    }

    private void goToPage(String page, ActionEvent ae){
        try{
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page)));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setTitle(MAIN_TITLE + " - " + page.substring(12, page.length()-5));
        } catch (IOException e){
            System.out.println("FXML: " + page + " not found");
        }
    }

    /**
                                                    * METHODS FOR LOGIN PAGE
     */
    @FXML public TextField userTextField; @FXML public PasswordField passwdField;
    @FXML public Button loginBtn; @FXML public Button newAccBtn;

    private UserApp userApp;


    public void goToLoginPage(ActionEvent ae) {
        goToPage("/gui/scenes/login.fxml", ae);
    }


    public void login(ActionEvent ae){

        if (userApp.retrieveUser(this.userTextField.getText(), this.passwdField.getText())){
            // store the current user in the UserApp class
            userApp.user = new UserApp.CurrentUser(this.userTextField.getText(), this.passwdField.getText());
            goToMainMenu(ae);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Failed");
            alert.setContentText("Username or Password incorrect");
            alert.showAndWait();
        }
    }

    @FXML
    private void newAccount(){
        String user = this.userTextField.getText();
        String passwd = this.passwdField.getText();
        userApp.addUser(user, passwd);
    }

    /**
                                                    * METHODS FOR MAIN MENU
     */

    @FXML public Label usrWelcome;

    /*
     * This method is called when the user is directed to the main menu
     */
    public void goToMainMenu(ActionEvent ae){
        goToPage("/gui/scenes/menu-page.fxml", ae);
        usrWelcome = (Label) scene.lookup("#usrWelcome");
        usrWelcome.setText("Welcome " + userApp.user.getUsername());
    }


    /**
     * This method is called when the user is directed to the Chat Bot page
     * @param ae event
     */
    public void goToChatBot(ActionEvent ae){
        goToPage("/gui/scenes/chat-page.fxml", ae);
        setCurrentType();
    }

    /**
     * This method is called when the user is directed to the Chat Bot page
     * @param ae event
     */
    public void goToSkillEditor(ActionEvent ae){
        goToPage("/gui/scenes/skill-editor.fxml", ae);
        setCurrentType();
    }

    /**
     * This method is called when the user is directed to the Settings page
     * @param ae event
     */
    public void goToSettings(ActionEvent ae){
        goToPage("/gui/scenes/settings.fxml", ae);
        setCurrentType();
    }


    /**
                                                    * METHODS FOR CHAT BOT
     */

    @FXML public ScrollPane scrollPane; @FXML private VBox chatBox; @FXML private TextField userInput;

    // This method is called when the user presses the 'Submit' button
    public void addMessageToChat() {
        String sentence = userInput.getText();
        HumanLabel humanLabel = new HumanLabel(sentence);
        String botString = getBotResponse(sentence);

        BotLabel botLabel = new BotLabel(botString);
        chatBox.getChildren().add(humanLabel);
        chatBox.getChildren().add(botLabel);
        int currentUserId = Conversationdb.getCurrentUserId(connection, "Tom", "tom12345"); //dummy code do not bother. It will be replaced by
        Conversationdb.storeConversation(connection, sentence, botString, currentUserId);
        userInput.clear();
    }

    /**
     * This method handles the response of the Bot to a given input
     * @param sentence question or statement from the user
     * @return response from the Bot
     */
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

    @FXML private TextArea fileTextArea; @FXML private Label editorMessage; @FXML private ProgressBar progressBar;
    @FXML private Button resetButton; @FXML private Button saveBtn;

    public static FileTime lastModifiedTime;
    public static File fileInEditor;

    /**
     * This method is called when the user presses the 'Open File' button
     */
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        // Select only .txt files
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
        );
        // Select Resources folder as initial directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/src/main/resources/texts"));
        File fileToLoad = fileChooser.showOpenDialog(null);
        // Load chosen fileToCheck
        if(fileToLoad != null){
            updateTextArea(fileToLoad);
        }
        // Set last modified time else scheduleFileChecking() will not work
        lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis());
        // Enable save button
        saveBtn.setDisable(false);
    }

    /**
     * This method updates the TextArea with the content of the selected fileToCheck
     * @param fileToLoad selected fileToCheck
     */
    private void updateTextArea(File fileToLoad) {
        progressBar.setVisible(true);
        Task<String> loadFileTask = fileLoaderTask(fileToLoad);
        progressBar.progressProperty().bind(loadFileTask.progressProperty());
        loadFileTask.run();
    }

    /**
     * This method loads the content of the selected fileToCheck into a Task
     * @param fileToLoad selected fileToCheck
     * @return Task with the content of the selected fileToCheck
     */
    private Task<String> fileLoaderTask(File fileToLoad){
        // Load content of the fileToCheck with a Task
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
                fileTextArea.setText("Could not load fileToCheck from:\n " + fileToLoad.getAbsolutePath());
            }
            // Check changes in the fileToCheck
            setFileChecking(fileInEditor);
        });
        //If task failed display error message
        loadFileTask.setOnFailed(workerStateEvent -> {
            fileTextArea.setText("Could not load fileToCheck from:\n " + fileToLoad.getAbsolutePath());
            editorMessage.setText("Failed to load fileToCheck");
        });
        return loadFileTask;
    }

    /**
     * This method sets a Scheduled Service to the loaded fileToCheck
     * The service checks if the fileToCheck has been modified
     * @param fileToCheck selected fileToCheck
     */
    private void setFileChecking(File fileToCheck){
        ScheduledService<Boolean> fileChecking = scheduledFileChecker(fileToCheck);
        //System.out.println(fileChecking.getLastValue());
        fileChecking.setOnSucceeded(workerStateEvent -> {
            if(fileChecking.getLastValue()==null) return;
            if(fileChecking.getLastValue()){
                // If changes are detected no need to continue checking
                fileChecking.cancel();
                // Show the button to reset the changes
                resetButton.setVisible(true);
            }
        });
        fileChecking.start();
    }


    /**
     * This method creates a Scheduled Service to check if the fileToCheck has been modified
     * @param FileToCheck selected fileToCheck
     * @return Scheduled Service to check if the fileToCheck has been modified
     */
    private ScheduledService<Boolean> scheduledFileChecker(File FileToCheck){
        ScheduledService<Boolean> scheduledService = new ScheduledService<>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        FileTime lastModifiedAsOfNow = Files.readAttributes(FileToCheck.toPath(), BasicFileAttributes.class).lastModifiedTime();
                        return lastModifiedAsOfNow.compareTo(lastModifiedTime) < 0;
                    }
                };
            }
        };
        // set the time step for each check
        scheduledService.setPeriod(Duration.seconds(5));
        return scheduledService;
    }

    /*
     * This method is called when the user presses the 'Reset Changes' button
     */
    public void resetChanges(){
        updateTextArea(fileInEditor);
        resetButton.setVisible(false);
    }

    /**
     * This method is called when the user presses the 'Save' button
     */
    public void saveFile() {
        try {
            FileWriter fw = new FileWriter(fileInEditor);
            fw.write(fileTextArea.getText());
            fw.close();
            lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis());
            setMessageSaved();
        } catch (IOException e) {
            System.out.println("Error in saving fileToCheck");
        }
    }

    /**
     * This method sets the status message to last saved time
     */
    private void setMessageSaved(){
        editorMessage.setText("File : '" + fileInEditor.getName() +
                "' saved at: " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    /**
                                                    * METHODS FOR SETTINGS
     */

    @FXML private ComboBox<String> botComboBox = new ComboBox<>();

    /**
     * Enum class for the different Bot types
     */

    public static final ObservableList<String> typeList = FXCollections.observableArrayList(typeNames());

    public enum BotType{
        CFG,
        TemplateSkills
    }

    /**
     * This method returns the Bot Types as an Array of Strings.
     * This is used to populate the ComboBox in the settings page
     * @return Array of Strings with the Bot Types
     */
    private static String[] typeNames(){
        String[] s = new String[BotType.values().length];
        for (int i = 0; i < BotType.values().length; i++) {
            s[i] = BotType.values()[i].name();
        }
        return s;
    }

    /**
     * This method is used to set the  current Bot Type to the selected type in the ComboBox
     */
    private void setCurrentType(){
        switch (botComboBox.getValue()){
            case "CFG" -> currentType = BotType.CFG;
            case "TemplateSkills" -> currentType = BotType.TemplateSkills;
            default -> currentType = BotType.TemplateSkills;
        }
        //System.out.println("Current bot type: " + currentType);
    }
}