package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillsManagement {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void addSkill(int userId, String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            System.out.println(ERROR_COLOR + "Skill cannot be empty." + RESET_COLOR);
            return;
        }

        String query = "INSERT INTO user_skills (user_id, skill) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, skill);
            stmt.executeUpdate();
            System.out.println(SUCCESS_COLOR + "Skill added successfully!" + RESET_COLOR);
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error adding skill: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    public static void removeSkill(int userId, String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            System.out.println(ERROR_COLOR + "Skill cannot be empty." + RESET_COLOR);
            return;
        }

        String query = "DELETE FROM user_skills WHERE user_id = ? AND skill = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, skill);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(SUCCESS_COLOR + "Skill removed successfully!" + RESET_COLOR);
            } else {
                System.out.println(ERROR_COLOR + "Failed to remove skill. Skill may not exist." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error removing skill: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }

    public static void viewSkills(int userId) {
        String query = "SELECT skill FROM user_skills WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean skillsFound = false;
            while (resultSet.next()) {
                skillsFound = true;
                System.out.println("Skill: " + resultSet.getString("skill"));
            }
            if (!skillsFound) {
                System.out.println(ERROR_COLOR + "No skills found for user." + RESET_COLOR);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error viewing skills: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}
