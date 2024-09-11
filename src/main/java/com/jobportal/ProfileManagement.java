package com.jobportal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileManagement {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    // Create or update user profile with resume as binary data
    public static void createOrUpdateProfile(int userId, String education, String skills, String achievements, String resumeFilePath) {
        if ((education == null || education.trim().isEmpty()) ||
            (skills == null || skills.trim().isEmpty()) ||
            (achievements == null || achievements.trim().isEmpty())) {
            System.out.println(ERROR_COLOR + "All profile fields (education, skills, achievements) must be completed." + RESET_COLOR);
            return;
        }

        String query = "INSERT INTO user_profiles (user_id, education, skills, achievements, resume) " +
                       "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                       "education = VALUES(education), skills = VALUES(skills), achievements = VALUES(achievements), resume = VALUES(resume)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             FileInputStream fis = new FileInputStream(new File(resumeFilePath))) {

            stmt.setInt(1, userId);
            stmt.setString(2, education);
            stmt.setString(3, skills);
            stmt.setString(4, achievements);

            // Set the resume as binary data
            stmt.setBinaryStream(5, fis, new File(resumeFilePath).length());

            stmt.executeUpdate();
            System.out.println(SUCCESS_COLOR + "Profile and resume created/updated successfully!" + RESET_COLOR);

        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error creating/updating profile: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(ERROR_COLOR + "Error reading resume file: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    // View user profile
    public static void viewProfile(int userId) {
        String query = "SELECT education, skills, achievements, resume FROM user_profiles WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.println("Education: " + resultSet.getString("education"));
                System.out.println("Skills: " + resultSet.getString("skills"));
                System.out.println("Achievements: " + resultSet.getString("achievements"));
                
                // Get the resume binary data (for example, we can write it to a file)
                byte[] resumeBytes = resultSet.getBytes("resume");
                if (resumeBytes != null) {
                    System.out.println(SUCCESS_COLOR + "Resume retrieved successfully." + RESET_COLOR);
                    // Logic to save the binary data back to a file can be implemented here
                } else {
                    System.out.println(ERROR_COLOR + "No resume found for this user." + RESET_COLOR);
                }
            } else {
                System.out.println(ERROR_COLOR + "Profile not found for user." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error viewing profile: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    // Check if the profile is complete (including the resume)
public static boolean isProfileComplete(int userId) {
    String query = "SELECT education, skills, achievements, resume FROM user_profiles WHERE user_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, userId);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            String education = resultSet.getString("education");
            String skills = resultSet.getString("skills");
            String achievements = resultSet.getString("achievements");
            byte[] resume = resultSet.getBytes("resume");  // Fetch resume data

            // Check if education, skills, achievements, and resume are not null or empty
            return education != null && !education.trim().isEmpty() &&
                   skills != null && !skills.trim().isEmpty() &&
                   achievements != null && !achievements.trim().isEmpty() &&
                   resume != null;  // Ensure the resume is uploaded
        }
    } catch (SQLException e) {
        System.out.println(ERROR_COLOR + "Error checking profile completeness: " + e.getMessage() + RESET_COLOR);
        e.printStackTrace();
    }
    return false;
}

}
