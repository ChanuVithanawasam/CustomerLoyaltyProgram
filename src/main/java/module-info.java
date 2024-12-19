module com.example.customerloyaltyprogram {
    requires javafx.controls;
    //requires javafx.fxml;
    requires java.sql;


    opens com.example.customerloyaltyprogram to javafx.fxml;
    exports com.example.customerloyaltyprogram;
}