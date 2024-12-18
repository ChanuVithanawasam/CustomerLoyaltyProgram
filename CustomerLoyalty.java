import java.util.HashMap;
import java.util.Scanner;

public class CustomerLoyalty {

    // A HashMap to store customer phone numbers and points
    static HashMap<String, Integer> customerPoints = new HashMap<>();

    // Conversion rate: 1 point = 1 rupee
    static final int POINT_TO_RUPEE = 1;

    // Method to add points to a customer
    public static void addPoints(String phoneNumber, int amountSpent) {
        int pointsToAdd = amountSpent / 10; // 10 rupees = 1 point
        customerPoints.put(phoneNumber, customerPoints.getOrDefault(phoneNumber, 0) + pointsToAdd);
        System.out.println("Points added successfully! Current points for " + phoneNumber + ": " + customerPoints.get(phoneNumber));
    }

    // Method to convert points into rupees
    public static void convertPoints(String phoneNumber) {
        if (customerPoints.containsKey(phoneNumber)) {
            int points = customerPoints.get(phoneNumber);
            int rupees = points * POINT_TO_RUPEE;
            System.out.println("Customer " + phoneNumber + " has " + points + " points, which equals " + rupees + " rupees.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    // Main menu to interact with the system
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Customer Loyalty Program ---");
            System.out.println("1. Add Points for a Customer");
            System.out.println("2. Convert Points to Rupees");
            System.out.println("3. View All Customers");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter Customer Phone Number: ");
                    String phoneNumber = scanner.nextLine();
                    System.out.print("Enter Amount Spent: ");
                    int amountSpent = scanner.nextInt();
                    addPoints(phoneNumber, amountSpent);
                    break;
                case 2:
                    System.out.print("Enter Customer Phone Number: ");
                    String phoneToConvert = scanner.nextLine();
                    convertPoints(phoneToConvert);
                    break;
                case 3:
                    System.out.println("All Customers and Their Points:");
                    for (String customer : customerPoints.keySet()) {
                        System.out.println("Phone: " + customer + ", Points: " + customerPoints.get(customer));
                    }
                    break;
                case 4:
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
