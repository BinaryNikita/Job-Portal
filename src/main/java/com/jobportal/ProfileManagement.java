package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileManagement {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    // Create or update user profile
    public static void createOrUpdateProfile(int userId, String education, String skills, String achievements) {
        if ((education == null || education.trim().isEmpty()) ||
            (skills == null || skills.trim().isEmpty()) ||
            (achievements == null || achievements.trim().isEmpty())) {
            System.out.println(ERROR_COLOR + "All profile fields (education, skills, achievements) must be completed." + RESET_COLOR);
            return;
        }

        String query = "INSERT INTO user_profiles (user_id, education, skills, achievements) " +
                       "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                       "education = VALUES(education), skills = VALUES(skills), achievements = VALUES(achievements)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, education);
            stmt.setString(3, skills);
            stmt.setString(4, achievements);
            stmt.executeUpdate();
            System.out.println(SUCCESS_COLOR + "Profile created/updated successfully!" + RESET_COLOR);
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error creating/updating profile: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    // Check if the profile is complete
    public static boolean isProfileComplete(int userId) {
        String query = "SELECT education, skills, achievements FROM user_profiles WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String education = resultSet.getString("education");
                String skills = resultSet.getString("skills");
                String achievements = resultSet.getString("achievements");
                return education != null && !education.trim().isEmpty() &&
                       skills != null && !skills.trim().isEmpty() &&
                       achievements != null && !achievements.trim().isEmpty();
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error checking profile completeness: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
        return false;
    }

    // View user profile
    public static void viewProfile(int userId) {
        String query = "SELECT education, skills, achievements FROM user_profiles WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                System.out.println("Education: " + resultSet.getString("education"));
                System.out.println("Skills: " + resultSet.getString("skills"));
                System.out.println("Achievements: " + resultSet.getString("achievements"));
            } else {
                System.out.println(ERROR_COLOR + "Profile not found for user." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error viewing profile: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}
