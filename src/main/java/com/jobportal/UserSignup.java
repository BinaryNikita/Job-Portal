package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSignup {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void signup(String name, String email, String phone, String password) {
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || 
            phone == null || phone.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println(ERROR_COLOR + "All fields must be filled out." + RESET_COLOR);
            return;
        }

        if (!isEmailUnique(email)) {
            System.out.println(ERROR_COLOR + "Email is already in use." + RESET_COLOR);
            return;
        }

        String query = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Signup successful!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Signup failed!" + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error during signup: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    private static boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return false; // Email already exists
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error checking email uniqueness: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
        return true; // Email is unique
    }
}