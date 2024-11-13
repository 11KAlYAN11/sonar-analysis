package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VulnerableApp {

    private static final Logger logger = Logger.getLogger(VulnerableApp.class.getName());

    // Avoid hard-coded passwords - would be better to load from a secure vault or environment variable
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public static void main(String[] args) {
        VulnerableApp app = new VulnerableApp();
        Scanner scanner = new Scanner(System.in);

        logger.info("Enter username: ");
        String username = scanner.nextLine();

        logger.info("Enter password: ");
        String password = scanner.nextLine();

        if (app.login(username, password)) {
            logger.info("Login successful!");
        } else {
            logger.info("Login failed.");
        }

        scanner.close();
    }

    public boolean login(String username, String password) {
        Connection connection = null;
        Statement stmt = null;

        try {
            // Using environment variables or configuration management for credentials
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", DB_PASSWORD);
            stmt = connection.createStatement();

            // Preventing SQL Injection by using parameterized queries
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (var preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet rs = preparedStatement.executeQuery();
                return rs.next();
            }
        } catch (Exception e) {
            // Logging error with specific message and level
            logger.log(Level.SEVERE, "An error occurred during login", e);
            return false;
        } finally {
            // Properly closing resources using try-with-resources if possible
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error closing resources", e);
            }
        }
    }

    public void handleUserInput() {
    Scanner scanner = new Scanner(System.in);
    logger.info("Enter your age: ");
    int age;
    try {
        age = Integer.parseInt(scanner.nextLine());
        logger.info(String.format("You entered: %d", age));
        if (age > 18) {
            logger.info("You are an adult.");
        } else {
            logger.info("You are a minor.");
        }
    } catch (NumberFormatException e) {
        logger.log(Level.WARNING, "Invalid age entered", e);
    } finally {
        scanner.close();
    }
 }

}
