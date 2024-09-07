package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobAlerts {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void viewSavedJobAlerts(int userId) {
        String query = "SELECT * FROM job_alerts WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean hasAlerts = false;
            while (resultSet.next()) {
                hasAlerts = true;
                System.out.println("Alert ID: " + resultSet.getInt("alert_id"));
                System.out.println("Job Title: " + resultSet.getString("job_title"));
                System.out.println("Location: " + resultSet.getString("location"));
                System.out.println("Job Type: " + resultSet.getString("job_type"));
                System.out.println();
            }
            if (!hasAlerts) {
                System.out.println("No job alerts found.");
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error viewing job alerts: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    public static void addNewJobAlert(int userId, String jobTitle, String location, String jobType) {
        String query = "INSERT INTO job_alerts (user_id, job_title, location, job_type) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, jobTitle);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, jobType);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Job alert added successfully!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Failed to add job alert." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error adding job alert: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}
