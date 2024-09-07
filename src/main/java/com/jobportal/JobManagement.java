package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobManagement {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void postJob(String title, String company, String location, String salaryRange, String description) {
        if (title.isEmpty() || company.isEmpty() || location.isEmpty() || salaryRange.isEmpty() || description.isEmpty()) {
            System.out.println(ERROR_COLOR + "All fields must be filled out." + RESET_COLOR);
            return;
        }

        String query = "INSERT INTO jobs (title, company, location, salary_range, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, company);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, salaryRange);
            preparedStatement.setString(5, description);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Job posted successfully!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Failed to post job." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error posting job: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    public static void updateJob(int jobId, String title, String company, String location, String salaryRange, String description) {
        if (jobId <= 0 || title.isEmpty() || company.isEmpty() || location.isEmpty() || salaryRange.isEmpty() || description.isEmpty()) {
            System.out.println(ERROR_COLOR + "Invalid input data." + RESET_COLOR);
            return;
        }

        String query = "UPDATE jobs SET title = ?, company = ?, location = ?, salary_range = ?, description = ? WHERE job_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, company);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, salaryRange);
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, jobId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Job updated successfully!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Failed to update job or job does not exist." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error updating job: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    public static void deleteJob(int jobId) {
        if (jobId <= 0) {
            System.out.println(ERROR_COLOR + "Invalid job ID." + RESET_COLOR);
            return;
        }

        String query = "DELETE FROM jobs WHERE job_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, jobId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Job deleted successfully!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Failed to delete job or job does not exist." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error deleting job: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    public static void viewApplicants(int jobId) {
        if (jobId <= 0) {
            System.out.println(ERROR_COLOR + "Invalid job ID." + RESET_COLOR);
            return;
        }
    
        String query = "SELECT * FROM applications WHERE job_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, jobId);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                System.out.println(ERROR_COLOR + "No applicants found for this job." + RESET_COLOR);
                return;
            }
    
            while (resultSet.next()) {
                System.out.println("Applicant ID: " + resultSet.getInt("applicant_id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Application Date: " + resultSet.getDate("application_date"));
                System.out.println("Status: " + resultSet.getString("status"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error retrieving applicants: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}    