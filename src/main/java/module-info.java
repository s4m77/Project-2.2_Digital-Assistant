module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.jsoup;


    opens gui to javafx.fxml;
    exports gui;
}