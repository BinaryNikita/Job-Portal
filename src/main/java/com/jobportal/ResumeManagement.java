package com.jobportal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResumeManagement {
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void viewResume(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                System.out.println(SUCCESS_COLOR + "Viewing resume: " + filePath + RESET_COLOR);
                // Read and display the resume content (for simplicity, just showing file size here)
                long fileSize = file.length();
                System.out.println("File Size: " + fileSize + " bytes");
                // Additional logic to display file content can be implemented here
            } catch (IOException e) {
                System.out.println(ERROR_COLOR + "Error reading resume file: " + e.getMessage() + RESET_COLOR);
                e.printStackTrace();
            }
        } else {
            System.out.println(ERROR_COLOR + "Resume file not found or is not a valid file." + RESET_COLOR);
        }
    }

    public static void uploadResume(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            // Implement logic to upload or update the resume file
            // For example, copy the file to a designated directory or update database record
            System.out.println(SUCCESS_COLOR + "Resume uploaded successfully!" + RESET_COLOR);
        } else {
            System.out.println(ERROR_COLOR + "Resume file not found or is not a valid file." + RESET_COLOR);
        }
    }
}
