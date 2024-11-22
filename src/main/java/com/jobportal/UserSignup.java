package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class UserSignup {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    // Regex patterns for validation
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    // Signup method which validates each step individually
    public static boolean signup(String name, String email, String phone, String password) {
        // Validate name: should only contain letters and spaces
        if (!validateName(name)) {
            return false;
        }

        // Validate email format
        if (!validateEmail(email)) {
            return false;
        }

        // Validate phone number: should only contain 10 digits
        if (!validatePhone(phone)) {
            return false;
        }

        // Check if email is unique
        if (!isEmailUnique(email)) {
            System.out.println(ERROR_COLOR + "Email is already in use." + RESET_COLOR);
            return false;
        }

        // Perform signup based on email domain (admin or regular user)
        if (email.endsWith("@jobPortal.com")) {
            return registerAdmin(name, email, phone, password);
        } else {
            return registerUser(name, email, phone, password);
        }
    }

    // Helper method for validating name
    private static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty() || !NAME_PATTERN.matcher(name).matches()) {
            System.out.println(ERROR_COLOR + "Invalid name. Only letters and spaces are allowed." + RESET_COLOR);
            return false;
        }
        return true;
    }

    // Helper method for validating email
    private static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println(ERROR_COLOR + "Invalid email format." + RESET_COLOR);
            return false;
        }
        return true;
    }

    // Helper method for validating phone number
    private static boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty() || !PHONE_PATTERN.matcher(phone).matches()) {
            System.out.println(ERROR_COLOR + "Invalid phone number. It should contain exactly 10 digits." + RESET_COLOR);
            return false;
        }
        return true;
    }

    // Register a regular user in the database
    private static boolean registerUser(String name, String email, String phone, String password) {
        String query = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "User signup successful!" + RESET_COLOR);
                return true;
            } else {
                System.out.println(ERROR_COLOR + "User signup failed!" + RESET_COLOR);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error during user signup: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
            return false;
        }
    }

    // Register an admin in the database
    private static boolean registerAdmin(String name, String email, String phone, String password) {
        String query = "INSERT INTO admins (name, email, contact, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Admin signup successful!" + RESET_COLOR);
                return true;
            } else {
                System.out.println(ERROR_COLOR + "Admin signup failed!" + RESET_COLOR);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error during admin signup: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
            return false;
        }
    }

    // Check if email is unique in both users and admins tables
    private static boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ? UNION SELECT COUNT(*) FROM admins WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return false; 
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error checking email uniqueness: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
        return true;
    }
}
