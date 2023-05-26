package gui.utils;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class FileEditor {

    private final Handler handler;

    FileEditor(Handler handler) {
        this.handler = handler;
    }

    protected void updateTextArea(File fileToLoad) {
        handler.progressBar.setVisible(true);
        Task<String> loadFileTask = fileLoaderTask(fileToLoad);
        handler.progressBar.progressProperty().bind(loadFileTask.progressProperty());
        loadFileTask.run();
    }

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
                handler.fileTextArea.setText(loadFileTask.get());
                handler.editorMessage.setText("File loaded: " + fileToLoad.getName());
                Handler.fileInEditor = fileToLoad;
            } catch (InterruptedException | ExecutionException e) {
                handler.fileTextArea.setText("Could not load fileToCheck from:\n " + fileToLoad.getAbsolutePath());
            }
            // Check changes in the fileToCheck
            setFileChecking(Handler.fileInEditor);
        });
        //If task failed display error message
        loadFileTask.setOnFailed(workerStateEvent -> {
            handler.fileTextArea.setText("Could not load fileToCheck from:\n " + fileToLoad.getAbsolutePath());
            handler.editorMessage.setText("Failed to load fileToCheck");
        });
        return loadFileTask;
    }

    private void setFileChecking(File file){
        ScheduledService<Boolean> fileChecking = scheduledFileChecker(file);
        //System.out.println(fileChecking.getLastValue());
        fileChecking.setOnSucceeded(workerStateEvent -> {
            if(fileChecking.getLastValue()==null) return;
            if(fileChecking.getLastValue()){
                // If changes are detected no need to continue checking
                fileChecking.cancel();
                // Show the button to reset the changes
                handler.resetButton.setVisible(true);
            }
        });
        fileChecking.start();
    }

    private ScheduledService<Boolean> scheduledFileChecker(File file){
        ScheduledService<Boolean> scheduledService = new ScheduledService<>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        FileTime lastModifiedAsOfNow = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
                        return lastModifiedAsOfNow.compareTo(Handler.lastModifiedTime) < 0;
                    }
                };
            }
        };
        // set the time step for each check
        scheduledService.setPeriod(Duration.seconds(5));
        return scheduledService;
    }


}
