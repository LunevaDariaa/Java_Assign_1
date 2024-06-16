module com.example.skincareapp {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.skincareapp to javafx.fxml;
    exports com.example.skincareapp;
}