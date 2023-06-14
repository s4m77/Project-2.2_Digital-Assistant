package gui.utils;

import FacialRecognision.FacialRecognition;
import bots.CFG.CFG;
import bots.TemplateSkills.TemplateSkills;
import db.Conversationdb;
import gui.utils.messages.BotLabel;
import gui.utils.messages.HumanLabel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nlp.SpellCheck;


import java.io.*;
import java.net.URL;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is the main controller for the GUI.
 * It handles all the events and actions that are triggered by the user.
 * Path to .fxml Files: /src/main/resources/gui/scenes
 */
public class Handler implements Initializable {

    public static final String MAIN_TITLE = "Multi-Modal Digital Assistant";

    private static final FacialRecognition fr = FacialRecognition.getInstance();
    private final Connection connection = Conversationdb.CreateServer();

    private Stage stage;
    private Scene scene;
    private Parent root;

    private static BotType currentType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nu.pattern.OpenCV.loadLocally(); // init openCV

        initBotCBox(); // init bot combobox

        initFaceCBox(); // init face combobox

        SpellCheck.setDistance(SpellCheck.Distance.EDIT); // init spellcheck
    }

    private void initFaceCBox(){
        faceComboBox.getItems().addAll(faceModelList);
        if (fr.currentModel != null)
            faceComboBox.setValue(String.valueOf(fr.currentModel));
        else
            faceComboBox.setValue(faceModelList.get(0));
        setCurrentModel();
    }

    private void initBotCBox() {
        botComboBox.getItems().addAll(typeList);
        if (currentType != null)
            botComboBox.setValue(String.valueOf(currentType));
        else
            botComboBox.setValue(typeList.get(0));
        setCurrentType();
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

    private final UserApp userApp = new UserApp(this.connection);

    public void login(ActionEvent ae){

        if (this.userApp.retrieveUser(this.userTextField.getText(), this.passwdField.getText())){
            // store the current user in the UserApp class
            this.userApp.storeUser(this.userTextField.getText(), this.passwdField.getText());
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
        this.userApp.addUser(user, passwd);
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
        usrWelcome.setText("Welcome " + UserApp.userInfo[0]);
        setCurrentType();
        setCurrentModel();
    }


    /**
     * This method is called when the user is directed to the Chat Bot page
     * @param ae event
     */
    public void goToChatBot(ActionEvent ae){
        goToPage("/gui/scenes/chat-page.fxml", ae);
        setCurrentType();
        setCurrentModel();
    }

    /**
     * This method is called when the user is directed to the Chat Bot page
     * @param ae event
     */
    public void goToSkillEditor(ActionEvent ae){
        goToPage("/gui/scenes/skill-editor.fxml", ae);
        setCurrentType();
        setCurrentModel();
    }

    /**
     * This method is called when the user is directed to the Settings page
     * @param ae event
     */
    public void goToSettings(ActionEvent ae){
        goToPage("/gui/scenes/settings.fxml", ae);
        setCurrentType();
        setCurrentModel();
    }

    /**
     * This method is called when the user is directed to the Login page (by clicking the 'Logout' button)
     * @param ae event
     */
    public void goToLoginPage(ActionEvent ae) {
        goToPage("/gui/scenes/login.fxml", ae);
    }

    /**
                                                    * METHODS FOR CHAT BOT
     */

    @FXML public ScrollPane scrollPane; @FXML private VBox chatBox; @FXML private TextField userInput;

    // This method is called when the user presses the 'Submit' button
    public void processInput() {
        System.out.println("User input: " + userInput.getText());
        // check if someone is in front of the camera
        System.out.println("Inside processInput()");
        if(fr.peopleInCamera()){
            addMessageToChat();
        } else { // if no one is in front of the camera, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("The system didn't detect anyone in the camera");
            alert.setContentText("Please make sure someone is in the camera");
            alert.showAndWait();
        }
    }

    /**
     * This method takes care of adding the questions and answers to the GUI.
     * Texts are Labels encapsulated in HBoxes, so they can be aligned to the left or right,
     * depending on User or Bot.
     */
    public void addMessageToChat(){
        String sentence = userInput.getText();
        HumanLabel humanLabel = new HumanLabel(sentence);
        String botString = getBotResponse(sentence);
        BotLabel botLabel = new BotLabel(botString);

        HBox humanHBox = new HBox();
        humanHBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(humanHBox, Priority.ALWAYS);
        humanHBox.prefWidthProperty().bind(chatBox.widthProperty().add(20));
        humanHBox.getChildren().addAll(humanLabel);

        HBox botHBox = new HBox();
        botHBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(botHBox, Priority.ALWAYS);
        botHBox.prefWidthProperty().bind(chatBox.widthProperty().subtract(20));
        botHBox.getChildren().addAll(botLabel);
        chatBox.getChildren().addAll(humanHBox, botHBox);

        Conversationdb.storeConversation(connection, sentence, botString, Integer.parseInt(UserApp.userInfo[2]));
        userInput.clear();
    }

    /**
     * This method handles the response of the Bot to a given input
     * @param sentence question or statement from the user
     * @return response from the Bot
     */
    private String getBotResponse(String sentence) {

        try {
            switch (currentType){
                case CFG -> {
                    String answer = CFG.interpret(sentence);
                    if (answer.equals("I have no idea")) {
                        String corr = SpellCheck.correctSentence(sentence);
                        return CFG.interpret(corr);
                    } else {
                        return answer;
                    }
                }
                case TemplateSkills -> {
                    return TemplateSkills.interpret(sentence);
                }
                default -> {
                    return "Error: BotType not set";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: BotType not set";
        }
    }

    /**
                                                    * METHODS FOR SKILL EDITOR
     */

    @FXML protected TextArea fileTextArea; @FXML protected Label editorMessage; @FXML protected ProgressBar progressBar;
    @FXML protected Button resetButton; @FXML protected Button saveBtn;

    protected static FileTime lastModifiedTime;
    protected static File fileInEditor;

    public static FileUploader uploader;

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
            uploader = new FileUploader(this);
            uploader.updateTextArea(fileToLoad);
            //new FileEditor(this).updateTextArea(fileToLoad);
        }
        // Set last modified time else scheduleFileChecking() will not work
        lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis());
        // Enable save button
        saveBtn.setDisable(false);
    }

    /*
     * This method is called when the user presses the 'Reset Changes' button
     */
    public void resetChanges(){
        uploader.updateTextArea(fileInEditor);
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
            System.out.println("Error in saving file in editor");
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
    @FXML private ComboBox<String> faceComboBox = new ComboBox<>();
    /**
     * Enum class for the different Bot types
     */

    public static final ObservableList<String> typeList = FXCollections.observableArrayList(botTypeNames());
    public static final ObservableList<String> faceModelList = FXCollections.observableArrayList(faceModelNames());

    public enum BotType{
        CFG,
        TemplateSkills
    }

    /**
     * This method returns the Bot Types as an Array of Strings.
     * This is used to populate the ComboBox in the settings page
     * @return Array of Strings with the Bot Types
     */
    private static String[] botTypeNames(){
        String[] s = new String[BotType.values().length];
        for (int i = 0; i < BotType.values().length; i++) {
            s[i] = BotType.values()[i].name();
        }
        return s;
    }

    private static String[] faceModelNames(){
        String[] s = new String[FacialRecognition.FacialModel.values().length];
        for (int i = 0; i < FacialRecognition.FacialModel.values().length; i++) {
            s[i] = FacialRecognition.FacialModel.values()[i].name();
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

    private void setCurrentModel(){
        switch (faceComboBox.getValue()) {
            case "EYE" -> fr.setFacialModel(FacialRecognition.FacialModel.EYE);
            case "FACE" ->fr.setFacialModel(FacialRecognition.FacialModel.FACE);
            default -> fr.setFacialModel(FacialRecognition.FacialModel.FACE);
        }
    }
}