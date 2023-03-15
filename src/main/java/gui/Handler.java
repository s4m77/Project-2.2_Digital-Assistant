package gui;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class Handler {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // CHAT
    @FXML
    private VBox chatBox; private TextField userInput;

    // EDITOR
    @FXML
    private TextArea fileTextArea; private Label message; private ProgressBar progressBar; private Button saveButton;

    private File loadedFileReference;
    private FileTime lastModifiedTime;

    public String getUserInput(){
        return userInput.getText();
    }

    public void goToMainMenu(ActionEvent ae) {
        try{
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/start-page.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println("FXML not found");
        }
    }

    public void goToChatBot(ActionEvent ae) {
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/chat-page.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println("FXML not found");
        }
    }

    public void goToSkillEditor(ActionEvent ae){
        try {
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/scenes/skill-editor.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println("FXML not found");
        }
    }

    public void addMessageToChat(String txt, boolean HUMAN){
        Label l = new Label();
        if (HUMAN) {
            l.setText("YOU: "+txt);
            l.setStyle("-fx-background-color:#E6E6FA");
        }
        else {
            l.setText("BOT: "+txt);
            l.setStyle("-fx-background-color:#00FFFF");
        }
        chatBox.getChildren().add(l);
    }

    //TODO: fix directory
    public void chooseFile(ActionEvent ae) throws URISyntaxException {
        FileChooser fileChooser = new FileChooser();
        //only allow text files to be selected using chooser
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
        );
        //set initial directory somewhere user will recognise
        fileChooser.setInitialDirectory(new File(Objects.requireNonNull(ChatApplication.class.getResource("/txts")).toURI()));
        //let user select file
        File fileToLoad = fileChooser.showOpenDialog(null);
        //if file has been chosen, load it using asynchronous method (define later)
        if(fileToLoad != null){
            loadFileToTextArea(fileToLoad);
        }
    }

    public void saveChanges(){
        loadFileToTextArea(loadedFileReference);
        saveButton.setVisible(false);
    }

    private Task<String> fileLoaderTask(File fileToLoad){
        //Create a task to load the file asynchronously
        Task<String> loadFileTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                BufferedReader reader = new BufferedReader(new FileReader(fileToLoad));
                //Use Files.lines() to calculate total lines - used for progress
                long lineCount;
                try (Stream<String> stream = Files.lines(fileToLoad.toPath())) {
                    lineCount = stream.count();
                }
                //Load in all lines one by one into a StringBuilder separated by "\n" - compatible with TextArea
                String line;
                StringBuilder totalFile = new StringBuilder();
                long linesLoaded = 0;
                while((line = reader.readLine()) != null) {
                    totalFile.append(line);
                    totalFile.append("\n");
                    updateProgress(++linesLoaded, lineCount);
                }
                return totalFile.toString();
            }
        };
        //Success: update the text area, display a success message and store the loaded file reference
        loadFileTask.setOnSucceeded(workerStateEvent -> {
            try {
                fileTextArea.setText(loadFileTask.get());
                message.setText("File loaded: " + fileToLoad.getName());
                loadedFileReference = fileToLoad;
            } catch (InterruptedException | ExecutionException e) {
                Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, e);
                fileTextArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
            }
            // Check changes in the file
            scheduleFileChecking(loadedFileReference);
        });
        //No success: set text area with error message and status message to failed
        loadFileTask.setOnFailed(workerStateEvent -> {
            fileTextArea.setText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
            message.setText("Failed to load file");
        });
        return loadFileTask;
    }

    private void loadFileToTextArea(File fileToLoad) {
        Task<String> loadFileTask = fileLoaderTask(fileToLoad);
        progressBar.progressProperty().bind(loadFileTask.progressProperty());
        loadFileTask.run();
    }

    private ScheduledService<Boolean> createFileChangesCheckingService(File file){
        ScheduledService<Boolean> scheduledService = new ScheduledService<>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        FileTime lastModifiedAsOfNow = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
                        return lastModifiedAsOfNow.compareTo(lastModifiedTime) > 0;
                    }
                };
            }
        };
        // check every 10 seconds
        scheduledService.setPeriod(Duration.seconds(10));
        return scheduledService;
    }

    private void scheduleFileChecking(File file){
        ScheduledService<Boolean> fileChangeCheckingService = createFileChangesCheckingService(file);
        fileChangeCheckingService.setOnSucceeded(workerStateEvent -> {
            if(fileChangeCheckingService.getLastValue()==null) return;
            if(fileChangeCheckingService.getLastValue()){
                //stop checking for changes
                fileChangeCheckingService.cancel();
                showSaveButton();
            }
        });
        System.out.println("Starting Checking Service...");
        fileChangeCheckingService.start();
    }

    private void showSaveButton() {
        saveButton.setVisible(true);
    }

    public void closeApp(){
        System.exit(0);
    }
}
