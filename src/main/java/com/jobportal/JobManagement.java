package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobManagement {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m"; // Red
    private static final String RESET_COLOR = "\033[0m"; // Reset

    public static void postJob(String title, String company, String location, String salaryRange, String description, String experienceRequired) {

        String query = "INSERT INTO jobs (title, company, location, salary_range, description, admin_id, experience) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int adminId = getAdminId(AdminLogin.loggedInAdminEmail);
            preparedStatement.setString(1, title.trim());
            preparedStatement.setString(2, company.trim());
            preparedStatement.setString(3, location.trim());
            preparedStatement.setString(4, salaryRange.trim());
            preparedStatement.setString(5, description.trim());
            preparedStatement.setInt(6, adminId);
            preparedStatement.setString(7, experienceRequired.trim());
            

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

    private static int getAdminId(String adminEmail) throws SQLException {
        String query = "SELECT admin_id FROM admins WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, adminEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("admin_id");
            } else {
                throw new SQLException("Admin not found for email: " + adminEmail);
            }
        }
    }

    public static void updateJob(int jobId, String title, String company, String location, String salaryRange,
            String description, String experience) {
        if (jobId <= 0 || title.isEmpty() || company.isEmpty() || location.isEmpty() || salaryRange.isEmpty()
                || description.isEmpty()) {
            System.out.println(ERROR_COLOR + "Invalid input data." + RESET_COLOR);
            return;
        }
        String query = "UPDATE jobs SET title = ?, company = ?, location = ?, salary_range = ?, description = ?, experience = ? WHERE job_id = ? ";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, company);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, salaryRange);
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, jobId);
            preparedStatement.setString(7, experience);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Job updated successfully!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Failed to update job or job does not exist or you are not the owner."
                        + RESET_COLOR);
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
    
        String query = "SELECT users.user_id, users.name, users.email, user_profiles.education, user_profiles.skills, " +
                       "user_profiles.achievements, user_profiles.contact, user_profiles.resume_path, " +
                       "applications.application_date, applications.status " +
                       "FROM applications " +
                       "JOIN users ON applications.user_id = users.user_id " +
                       "JOIN user_profiles ON users.user_id = user_profiles.user_id " +
                       "WHERE applications.job_id = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, jobId);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (!resultSet.isBeforeFirst()) {
                System.out.println(ERROR_COLOR + "No applicants found for this job." + RESET_COLOR);
                return;
            }
    
            // Print table header
            System.out.printf("%-10s %-20s %-40s %-15s %-20s %-20s %-15s %-40s %-20s %-10s%n",
                    "User ID", "Name", "Email", "Education", "Skills", "Achievements", "Contact",
                    "Resume Path", "Application Date", "Status");
            System.out.println("---------------------------------------------------------------------------------------------------------------" +
                    "-----------------------------------------------------------------");
    
            // Print each applicant in tabular format
            while (resultSet.next()) {
                System.out.printf("%-10d %-20s %-30s %-15s %-40s %-20s %-15s %-50s %-20s %-10s%n",
                        resultSet.getInt("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("education"),
                        resultSet.getString("skills"),
                        resultSet.getString("achievements"),
                        resultSet.getString("contact"),
                        resultSet.getString("resume_path"),
                        resultSet.getDate("application_date"),
                        resultSet.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error retrieving applicants: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
    

    public static void viewPostedJobsByAdminEmail(String adminEmail) {
        String query = "SELECT jobs.* FROM jobs " +
                       "JOIN admins ON jobs.admin_id = admins.admin_id " +
                       "WHERE admins.email = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, adminEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (!resultSet.isBeforeFirst()) {
                System.out.println(ERROR_COLOR + "No jobs found for this admin email." + RESET_COLOR);
                return;
            }
    
            // Print table header
            System.out.printf("%-10s %-20s %-20s %-20s %-15s %-50s%n",
                    "Job ID", "Title", "Company", "Location", "Salary Range", "Description");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
    
            // Print each job in tabular format
            while (resultSet.next()) {
                System.out.printf("%-10d %-20s %-20s %-20s %-15s %-80s%n",
                        resultSet.getInt("job_id"),
                        resultSet.getString("title"),
                        resultSet.getString("company"),
                        resultSet.getString("location"),
                        resultSet.getString("salary_range"),
                        resultSet.getString("description"));
                        resultSet.getString("experience");
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error retrieving posted jobs: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}    