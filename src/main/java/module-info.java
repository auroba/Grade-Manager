module com.example.gradeManager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.healthmarketscience.jackcess;
    requires com.google.gson;


    opens com.example.gradeManager to javafx.fxml, com.google.gson;
    exports com.example.gradeManager;
}