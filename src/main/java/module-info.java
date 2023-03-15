module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;
    //requires org.apache.httpcomponents.httpclient;
    //requires org.apache.httpcomponents.httpcore;
    requires org.jsoup;

    opens gui to javafx.fxml;
    exports gui;

    //exports Calculator;


}