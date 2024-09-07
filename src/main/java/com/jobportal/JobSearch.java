package com.jobportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobSearch {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void searchJobs(String title, String location, String jobType, String salaryRange) {
        // Validate input
        if (title == null) title = "";
        if (location == null) location = "";
        if (salaryRange == null) salaryRange = "";

        // Construct query
        String query = "SELECT * FROM jobs WHERE title LIKE ? AND location LIKE ? AND salary_range LIKE ?";
        if (jobType != null && !jobType.isEmpty()) {
            query += " AND job_type = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            stmt.setString(paramIndex++, "%" + title + "%");
            stmt.setString(paramIndex++, "%" + location + "%");
            stmt.setString(paramIndex++, "%" + salaryRange + "%");
            if (jobType != null && !jobType.isEmpty()) {
                stmt.setString(paramIndex, jobType);
            }

            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                System.out.println(ERROR_COLOR + "No jobs found matching the criteria." + RESET_COLOR);
                return;
            }

            // Process result set
            while (rs.next()) {
                System.out.println("Job Title: " + rs.getString("title"));
                System.out.println("Company: " + rs.getString("company"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("Salary Range: " + rs.getString("salary_range"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(ERROR_COLOR + "Error during job search: " + e.getMessage() + RESET_COLOR);
            e.printStackTrace();
        }
    }
}
