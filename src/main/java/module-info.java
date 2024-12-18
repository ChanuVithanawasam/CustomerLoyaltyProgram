module com.example.customerloyaltyprogram {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.customerloyaltyprogram to javafx.fxml;
    exports com.example.customerloyaltyprogram;
}