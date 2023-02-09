module com.example.project_22_digitalassistant {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project_22_digitalassistant to javafx.fxml;
    exports com.example.project_22_digitalassistant;
}