module com.example.week10guidb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.week10guidb to javafx.fxml;
    exports com.example.week10guidb;
}