package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAppliedJobs {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void viewAppliedJobs(int userId) {
        String query = "SELECT * FROM applications WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                System.out.println("Application ID: " + resultSet.getInt("application_id"));
                System.out.println("Job ID: " + resultSet.getInt("job_id"));
                System.out.println("Application Date: " + resultSet.getDate("application_date"));
                System.out.println("Status: " + resultSet.getString("status"));
                System.out.println();
            }
            if (!hasResults) {
                System.out.println(SUCCESS_COLOR + "No jobs applied." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error viewing applied jobs: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}
