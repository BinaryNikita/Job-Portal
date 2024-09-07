package com.jobportal;

import java.util.Scanner;

public class JobPortalApp {
    private static Scanner scanner = new Scanner(System.in);
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m";   // Red
    private static final String RESET_COLOR = "\033[0m";      // Reset

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== Job Portal Console Clone ====");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int option = getValidIntegerInput();
            scanner.nextLine();
            

            switch (option) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleSignup();
                    break;
                case 3:
                    handleAdminLogin();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
            }
        }
    }

    private static void handleSignup() {
        System.out.println("\n==== Signup ====");
        System.out.print("Enter your Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine();

        UserSignup.signup(name, email, phone, password);
    }

    private static void handleLogin() {
        System.out.println("\n==== Login ====");
        System.out.print("Enter your Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter your Password: ");
        String password = scanner.nextLine().trim();
    
        // Check if email or password is empty
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email and password cannot be empty.");
            return;
        }
    
        // Proceed with login if fields are not empty
        if (UserLogin.login(email, password)) {
            showUserDashboard();
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }
    

    private static void handleJobSearch() {
        System.out.println("\n==== Search Job ====");
        System.out.print("Enter Job Title or Company Name: ");
        String title = scanner.nextLine();
        System.out.print("Filter by Location (optional): ");
        String location = scanner.nextLine();
        System.out.print("Filter by Job Type (full-time/part-time) (optional): ");
        String jobType = scanner.nextLine();
        System.out.print("Filter by Salary Range (optional): ");
        String salaryRange = scanner.nextLine();

        JobSearch.searchJobs(title, location, jobType, salaryRange);
    }

    private static void handleManageSkills() {
        System.out.println("\n==== Manage Skills ====");
        System.out.println("1. Add Skill");
        System.out.println("2. Remove Skill");
        System.out.println("3. View Skills");
        System.out.println("4. Go Back");
        System.out.print("Choose an option: ");

        int option = getValidIntegerInput();
        int userId = getUserId(); // Replace with actual user ID retrieval
        switch (option) {
            case 1:
                System.out.print("Enter Skill to Add: ");
                String addSkill = scanner.nextLine();
                SkillsManagement.addSkill(userId, addSkill);
                break;
            case 2:
                System.out.print("Enter Skill to Remove: ");
                String removeSkill = scanner.nextLine();
                SkillsManagement.removeSkill(userId, removeSkill);
                break;
            case 3:
                SkillsManagement.viewSkills(userId);
                break;
            case 4:
                return;
            default:
                System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
        }
    }

    private static void showUserDashboard() {
        while (true) {
            System.out.println("\n==== User Dashboard ====");
            System.out.println("1. Search Job");
            System.out.println("2. View Applied Jobs");
            System.out.println("3. Apply for Job");
            System.out.println("4. View Job Recommendations");
            System.out.println("5. Manage Skills");
            System.out.println("6. View Resume (Upload/Update)");
            System.out.println("7. View Job Alerts");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            int option = getValidIntegerInput();

            switch (option) {
                case 1:
                    handleJobSearch();
                    break;
                case 2:
                    handleViewAppliedJobs();
                    break;
                case 3:
                    handleJobApplication();
                    break;
                case 4:
                    handleViewJobRecommendations();
                    break;
                case 5:
                    handleManageSkills();
                    break;
                case 6:
                    handleViewOrUpdateResume();
                    break;
                case 7:
                    handleJobAlerts();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
            }
        }
    }

    private static void handleJobApplication() {
        System.out.println("\n==== Apply for Job ====");
        System.out.print("Enter Job ID: ");
        int jobId = getValidIntegerInput();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Cover Letter: ");
        String coverLetter = scanner.nextLine();
        System.out.print("Enter your Resume File Path: ");
        String resumePath = scanner.nextLine();

        int userId = getUserId(); // Replace with actual user ID retrieval
        JobApplication.applyForJob(userId, jobId, coverLetter, resumePath);
    }

    private static void handleViewAppliedJobs() {
        System.out.println("\n==== View Applied Jobs ====");
        int userId = getUserId(); // Replace with actual user ID retrieval
        ViewAppliedJobs.viewAppliedJobs(userId);
    }

    private static void handleViewJobRecommendations() {
        System.out.println("\n==== View Job Recommendations ====");
        int userId = getUserId(); // Replace with actual user ID retrieval
        JobRecommendations.viewJobRecommendations(userId);
    }

    private static void handleViewOrUpdateResume() {
        System.out.println("\n==== View/Update Resume ====");
        System.out.println("1. View Current Resume");
        System.out.println("2. Upload New Resume");
        System.out.println("3. Go Back");
        System.out.print("Choose an option: ");

        int option = getValidIntegerInput();

        switch (option) {
            case 1:
                System.out.print("Enter your Resume File Path: ");
                String viewPath = scanner.nextLine();
                ResumeManagement.viewResume(viewPath);
                break;
            case 2:
                System.out.print("Enter the New Resume File Path: ");
                String uploadPath = scanner.nextLine();
                ResumeManagement.uploadResume(uploadPath);
                break;
            case 3:
                return;
            default:
                System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
        }
    }

    private static void handleJobAlerts() {
        System.out.println("\n==== Job Alerts ====");
        System.out.println("1. View Saved Job Alerts");
        System.out.println("2. Add New Job Alert");
        System.out.println("3. Go Back");
        System.out.print("Choose an option: ");

        int option = getValidIntegerInput();
        int userId = getUserId(); // Replace with actual user ID retrieval
        switch (option) {
            case 1:
                JobAlerts.viewSavedJobAlerts(userId);
                break;
            case 2:
                System.out.print("Enter Job Title: ");
                String jobTitle = scanner.nextLine();
                System.out.print("Enter Location: ");
                String location = scanner.nextLine();
                System.out.print("Enter Job Type (full-time/part-time): ");
                String jobType = scanner.nextLine();
                JobAlerts.addNewJobAlert(userId, jobTitle, location, jobType);
                break;
            case 3:
                return;
            default:
                System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
        }
    }

    private static void showAdminDashboard() {
        while (true) {
            System.out.println("\n==== Admin Dashboard ====");
            System.out.println("1. Post a New Job");
            System.out.println("2. Update a Job Posting");
            System.out.println("3. Delete a Job Posting");
            System.out.println("4. View Applicants for Jobs");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");

            int option = getValidIntegerInput();

            switch (option) {
                case 1:
                    handlePostNewJob();
                    break;
                case 2:
                    handleUpdateJobPosting();
                    break;
                case 3:
                    handleDeleteJobPosting();
                    break;
                case 4:
                    handleViewApplicantsForJob();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
            }
        }
    }

    private static void handleAdminLogin() {
        System.out.println("\n==== Admin Login ====");
        System.out.print("Enter Admin Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        if (AdminLogin.login(email, password)) {
            showAdminDashboard();
        } else {
            System.out.println(ERROR_COLOR + "Invalid credentials. Please try again." + RESET_COLOR);
        }
    }

    private static void handlePostNewJob() {
        System.out.println("\n==== Post a New Job ====");
        System.out.print("Enter Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Company Name: ");
        String company = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Salary Range: ");
        String salaryRange = scanner.nextLine();
        System.out.print("Enter Job Description: ");
        String description = scanner.nextLine();

        JobManagement.postJob(title, company, location, salaryRange, description);
    }

    private static void handleUpdateJobPosting() {
        System.out.println("\n==== Update a Job Posting ====");
        System.out.print("Enter Job ID to Update: ");
        int jobId = getValidIntegerInput();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Job Details (or skip to keep unchanged):\n");
        System.out.print("Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Company Name: ");
        String company = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Salary Range: ");
        String salaryRange = scanner.nextLine();
        System.out.print("Job Description: ");
        String description = scanner.nextLine();

        JobManagement.updateJob(jobId, title, company, location, salaryRange, description);
    }

    private static void handleDeleteJobPosting() {
        System.out.println("\n==== Delete a Job Posting ====");
        System.out.print("Enter Job ID to Delete: ");
        int jobId = getValidIntegerInput();
        scanner.nextLine(); // Consume newline

        System.out.print("Are you sure? (Y/N): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            JobManagement.deleteJob(jobId);
        }
    }

    private static void handleViewApplicantsForJob() {
        System.out.println("\n==== View Applicants for Job ====");
        System.out.print("Enter Job ID: ");
        int jobId = getValidIntegerInput();
        scanner.nextLine(); // Consume newline

        JobManagement.viewApplicants(jobId);
    }

    // Method to get valid integer input
    private static int getValidIntegerInput() {
        while (!scanner.hasNextInt()) {
            System.out.println(ERROR_COLOR + "Invalid input. Please enter a valid number." + RESET_COLOR);
            scanner.next(); // Clear invalid input
        }
        return scanner.nextInt();
    }

    // Placeholder method for retrieving the current user's ID
    private static int getUserId() {
        // Implement actual logic to retrieve the logged-in user's ID
        return 1; // Example user ID
    }
}
