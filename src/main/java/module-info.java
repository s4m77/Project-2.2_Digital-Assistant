module com.example.project_22_digitalassistant {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;

    exports com.example.project_22_digitalassistant;
    opens com.example.project_22_digitalassistant to javafx.fxml;
    exports Calculator;

}