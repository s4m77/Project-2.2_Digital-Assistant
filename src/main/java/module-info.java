module com.example.project_22_digitalassistant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;


    opens com.example.project_22_digitalassistant to javafx.fxml;
    exports com.example.project_22_digitalassistant;


}