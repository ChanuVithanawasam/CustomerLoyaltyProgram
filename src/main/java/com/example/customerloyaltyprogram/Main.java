package com.example.customerloyaltyprogram;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {

    private HashMap<String, Integer> customerPoints = new HashMap<>();
    private static final int POINTS_PER_RUPEE = 10;
    private static final int POINT_TO_RUPEE_CONVERSION = 1;

    private TextField phoneField, amountField;
    private TextArea outputArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Phone Shop Loyalty Program");

        // Input Fields and Labels
        Label phoneLabel = new Label("Customer Phone:");
        phoneField = new TextField();
        phoneField.setPromptText("Enter phone number");

        Label amountLabel = new Label("Amount Spent:");
        amountField = new TextField();
        amountField.setPromptText("Enter amount spent");

        // Buttons
        Button addPointsButton = new Button("Add Points");
        Button convertPointsButton = new Button("Convert Points");

        // Output Area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPromptText("Results will appear here...");

        // Layout Setup
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(phoneLabel, 0, 0);
        gridPane.add(phoneField, 1, 0);
        gridPane.add(amountLabel, 0, 1);
        gridPane.add(amountField, 1, 1);
        gridPane.add(addPointsButton, 0, 2);
        gridPane.add(convertPointsButton, 1, 2);

        VBox layout = new VBox(20, gridPane, outputArea);
        layout.setPadding(new Insets(20));

        // Apply CSS
        layout.setId("root");
        Scene scene = new Scene(layout, 500, 400);
        scene.getStylesheets().add("style.css");

        // Button Actions
        addPointsButton.setOnAction(e -> addPoints());
        convertPointsButton.setOnAction(e -> convertPoints());

        // Display Stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addPoints() {
        String phone = phoneField.getText().trim();
        String amountText = amountField.getText().trim();

        try {
            int amountSpent = Integer.parseInt(amountText);
            int pointsToAdd = amountSpent / POINTS_PER_RUPEE;

            if (!phone.isEmpty() && amountSpent > 0) {
                customerPoints.put(phone, customerPoints.getOrDefault(phone, 0) + pointsToAdd);
                outputArea.appendText("Added " + pointsToAdd + " points to customer " + phone + ".\n");
                outputArea.appendText("Current points: " + customerPoints.get(phone) + "\n\n");
            } else {
                outputArea.appendText("Please enter a valid phone number and amount.\n\n");
            }
        } catch (NumberFormatException ex) {
            outputArea.appendText("Amount must be a valid number.\n\n");
        }

        phoneField.clear();
        amountField.clear();
    }

    private void convertPoints() {
        String phone = phoneField.getText().trim();

        if (customerPoints.containsKey(phone)) {
            int points = customerPoints.get(phone);
            int rupees = points * POINT_TO_RUPEE_CONVERSION;

            outputArea.appendText("Customer " + phone + " has " + points + " points.\n");
            outputArea.appendText("Equivalent in rupees: " + rupees + " rupees.\n\n");
        } else {
            outputArea.appendText("Customer not found. Please add points first.\n\n");
        }

        phoneField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
