package com.jobportal;

import java.util.Scanner;

public class JobPortalApp {
    private static Scanner scanner = new Scanner(System.in);
    private static final String SUCCESS_COLOR = "\033[0;32m"; // Green
    private static final String ERROR_COLOR = "\033[0;31m"; // Red
    private static final String RESET_COLOR = "\033[0m"; // Reset
    private static String loggedInAdminEmail = "";

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== Job Portal Console Clone ====");
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int option = getValidIntegerInput();
            scanner.nextLine(); // Consume newline

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
            System.out.println(ERROR_COLOR + "Email and password cannot be empty." + RESET_COLOR);
            return;
        }

        // Proceed with login if fields are not empty
        if (UserLogin.login(email, password)) {
            showUserDashboard();
        } else {
            System.out.println(ERROR_COLOR + "Invalid email or password. Please try again." + RESET_COLOR);
        }
    }

    private static void showUserDashboard() {
        while (true) {
            System.out.println("\n==== User Dashboard ====");
            System.out.println("1. Create/Update Profile");
            System.out.println("2. Apply for Job");
            System.out.println("3. Search Job");
            System.out.println("4. View Applied Jobs");
            System.out.println("5. View Job Recommendations");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int option = getValidIntegerInput();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    handleProfileManagement();
                    break;
                case 2:
                    handleJobApplication();
                    break;
                case 3:
                    handleJobSearch();
                    break;
                case 4:
                    handleViewAppliedJobs();
                    break;
                case 5:
                    handleViewJobRecommendations();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
            }
        }
    }

    private static void handleProfileManagement() {
        System.out.println("\n==== Create/Update Profile ====");
        int userId = getUserId(); // Replace with actual user ID retrieval
        System.out.print("Enter Education: ");
        String education = scanner.nextLine();
        System.out.print("Enter Skills (comma-separated): ");
        String skills = scanner.nextLine();
        System.out.print("Enter Achievements: ");
        String achievements = scanner.nextLine();
        System.out.println("Enter you resume path: ");
        String resumePath = scanner.nextLine();

        ProfileManagement.createOrUpdateProfile(userId, education, skills, achievements, resumePath);
    }

    private static void handleJobSearch() {
        System.out.println("\n==== Search Job ====");
        System.out.print("Enter Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Filter by Location (optional): ");
        String location = scanner.nextLine();
        System.out.print("Filter by Job Type (full-time/part-time) (optional): ");
        String jobType = scanner.nextLine();
        System.out.print("Filter by Company Name: ");
        String company = scanner.nextLine();

        JobSearch.searchJobs(title, location, jobType, company);
    }

    private static void handleJobApplication() {
        int userId = getUserId();


        if (!ProfileManagement.isProfileComplete(userId)) {
            System.out.println(ERROR_COLOR + "Please complete your profile before applying for jobs." + RESET_COLOR);
            return;
        }

        System.out.println("\n==== Apply for Job ====");
        System.out.print("Enter Job ID: ");
        int jobId = getValidIntegerInput();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your Resume File Path: ");
        String resumePath = scanner.nextLine();

        JobApplication.applyForJob(userId, jobId, resumePath);
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

    private static void showAdminDashboard() {
        while (true) {
            System.out.println("\n==== Admin Dashboard ====");
            System.out.println("1. Post a New Job");
            System.out.println("2. Update a Job Posting");
            System.out.println("3. Delete a Job Posting");
            System.out.println("4. View Applicants for Jobs");
            System.out.println("5. View Posted Jobs");
            System.out.println("6. Logout");
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
                    handleViewPostedJobs();
                    break;
                case 6:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println(ERROR_COLOR + "Invalid option. Please try again." + RESET_COLOR);
            }
        }
    }

    private static void handlePostNewJob() {
        System.out.println("\n==== Post a New Job ====");
        scanner.nextLine();
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

    private static void handleViewPostedJobs() {
        System.out.println("\n==== View Posted Jobs ====");
        JobManagement.viewPostedJobsByAdminEmail(loggedInAdminEmail);
    }

    private static void handleAdminLogin() {
        System.out.println("\n==== Admin Login ====");
        System.out.print("Enter your Admin Email: ");
        String adminEmail = scanner.nextLine().trim();
        System.out.print("Enter your Password: ");
        String adminPassword = scanner.nextLine().trim();

        if (AdminLogin.login(adminEmail, adminPassword)) {
            loggedInAdminEmail = adminEmail; // Store logged-in admin email
            showAdminDashboard();
        } else {
            System.out.println(ERROR_COLOR + "Invalid admin email or password. Please try again." + RESET_COLOR);
        }
    }

    private static int getValidIntegerInput() {
        while (!scanner.hasNextInt()) {
            System.out.println(ERROR_COLOR + "Invalid input. Please enter a number." + RESET_COLOR);
            scanner.next(); // Clear invalid input
        }
        return scanner.nextInt();
    }

    private static int getUserId() {
        // Replace this stub with your actual user ID retrieval method
        return 1; // Example user ID
    }

}
