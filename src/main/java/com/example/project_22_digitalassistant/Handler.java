package com.example.project_22_digitalassistant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Handler {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private VBox chatBox;
    @FXML
    private TextField userInput;

    public String getUserInput(){
        return userInput.getText();
    }

    public void goToMainMenu(ActionEvent ae) {
        try{
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("start-page.fxml")));
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
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("chat-page.fxml")));
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
            root = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource("editor-page.fxml")));
            stage = (Stage) ((Node)ae.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println("FXML not found");
        }
    }

    public void addLabel(String txt, boolean HUMAN){
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


    public void closeApp(){
        System.exit(0);
    }
}
