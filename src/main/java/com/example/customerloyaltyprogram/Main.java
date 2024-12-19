package com.example.customerloyaltyprogram;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {
    private final String DB_URL = "jdbc:mysql://localhost:3306/loyalty_program";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "1234";

    @Override
    public void start(Stage primaryStage) {
        // UI Elements for Adding/Updating Customer Points
        Label nameLabel = new Label("Customer Name:");
        TextField nameField = new TextField();

        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();

        Label pointsLabel = new Label("Purchase Amount:");
        TextField pointsField = new TextField();

        Button submitButton = new Button("Submit");
        Label resultLabel = new Label();

        // UI Elements for Searching Customer
        Label searchPhoneLabel = new Label("Search by Phone Number:");
        TextField searchPhoneField = new TextField();
        Button searchButton = new Button("Search");
        Label searchResultLabel = new Label();

        // UI Elements for Deleting Customer
        Label deletePhoneLabel = new Label("Delete by Phone Number:");
        TextField deletePhoneField = new TextField();
        Button deleteButton = new Button("Delete");
        Label deleteResultLabel = new Label();

        // UI Elements for Showing All Customers
        Button showAllButton = new Button("Show All Customers");
        TextArea allCustomersArea = new TextArea();
        allCustomersArea.setEditable(false);

        // Event Handlers
        submitButton.setOnAction(event -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            int points = Integer.parseInt(pointsField.getText());

            try {
                updateCustomerPoints(name, phone, points);
                resultLabel.setText("Customer points updated successfully!");
            } catch (SQLException e) {
                resultLabel.setText("Error: " + e.getMessage());
            }
        });

        searchButton.setOnAction(event -> {
            String phone = searchPhoneField.getText();
            try {
                String customerDetails = searchCustomerByPhone(phone);
                searchResultLabel.setText(customerDetails);
            } catch (SQLException e) {
                searchResultLabel.setText("Error: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String phone = deletePhoneField.getText();
            try {
                deleteCustomer(phone);
                deleteResultLabel.setText("Customer deleted successfully!");
            } catch (SQLException e) {
                deleteResultLabel.setText("Error: " + e.getMessage());
            }
        });

        showAllButton.setOnAction(event -> {
            try {
                String allCustomers = getAllCustomers();
                allCustomersArea.setText(allCustomers);
            } catch (SQLException e) {
                allCustomersArea.setText("Error: " + e.getMessage());
            }
        });

        // Layout
        VBox vbox = new VBox(10,
                nameLabel, nameField,
                phoneLabel, phoneField,
                pointsLabel, pointsField,
                submitButton, resultLabel,
                new Separator(),
                searchPhoneLabel, searchPhoneField, searchButton, searchResultLabel,
                new Separator(),
                deletePhoneLabel, deletePhoneField, deleteButton, deleteResultLabel,
                new Separator(),
                showAllButton, allCustomersArea
        );
        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Loyalty Program");
        primaryStage.show();
    }

    private void updateCustomerPoints(String name, String phone, int points) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // Check if the customer exists
        String checkCustomerQuery = "SELECT points FROM customers WHERE phone = ?";
        PreparedStatement checkStmt = connection.prepareStatement(checkCustomerQuery);
        checkStmt.setString(1, phone);

        ResultSet resultSet = checkStmt.executeQuery();

        if (resultSet.next()) {
            // Update existing customer's points
            int existingPoints = resultSet.getInt("points");
            String updateQuery = "UPDATE customers SET points = ? WHERE phone = ?";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setInt(1, existingPoints + points);
            updateStmt.setString(2, phone);
            updateStmt.executeUpdate();
        } else {
            // Insert new customer
            String insertQuery = "INSERT INTO customers (name, phone, points) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setString(1, name);
            insertStmt.setString(2, phone);
            insertStmt.setInt(3, points);
            insertStmt.executeUpdate();
        }

        // Record transaction
        String transactionQuery = "INSERT INTO transactions (customer_phone, points_added) VALUES (?, ?)";
        PreparedStatement transactionStmt = connection.prepareStatement(transactionQuery);
        transactionStmt.setString(1, phone);
        transactionStmt.setInt(2, points);
        transactionStmt.executeUpdate();

        connection.close();
    }

    private String searchCustomerByPhone(String phone) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        String query = "SELECT name, points FROM customers WHERE phone = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, phone);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            int points = resultSet.getInt("points");
            connection.close();
            return "Name: " + name + ", Points: " + points;
        } else {
            connection.close();
            return "Customer not found.";
        }
    }

    private void deleteCustomer(String phone) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // Delete transactions first
        String deleteTransactionsQuery = "DELETE FROM transactions WHERE customer_phone = ?";
        PreparedStatement deleteTransactionsStmt = connection.prepareStatement(deleteTransactionsQuery);
        deleteTransactionsStmt.setString(1, phone);
        deleteTransactionsStmt.executeUpdate();

        // Then delete the customer
        String deleteCustomerQuery = "DELETE FROM customers WHERE phone = ?";
        PreparedStatement deleteCustomerStmt = connection.prepareStatement(deleteCustomerQuery);
        deleteCustomerStmt.setString(1, phone);
        deleteCustomerStmt.executeUpdate();

        connection.close();
    }

    private String getAllCustomers() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        String query = "SELECT name, phone, points FROM customers";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        StringBuilder result = new StringBuilder("All Customers:\n");
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String phone = resultSet.getString("phone");
            int points = resultSet.getInt("points");
            result.append("Name: ").append(name)
                    .append(", Phone: ").append(phone)
                    .append(", Points: ").append(points)
                    .append("\n");
        }

        connection.close();
        return result.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
