package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class JobApplication {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void applyForJob(int userId, int jobId, String coverLetter, String resumePath) {
        // Validate input (pseudo code)
        if (userId <= 0 || jobId <= 0) {
            System.out.println(ERROR_COLOR + "Invalid user ID or job ID." + RESET_COLOR);
            return;
        }

        String query = "INSERT INTO applications (user_id, job_id, application_date, status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, jobId);
            preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.setString(4, "Submitted");

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Application submitted successfully!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Application submission failed." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error during job application: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}