package auth;

/**
 * Simple, hardcoded authentication utility for admin access.
 * NOTE: In a real application, this would use a database table
 * and secure password hashing (like BCrypt).
 */
public class AdminAuth {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin"; // Dummy password

    /**
     * Authenticates the given username and password.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return true if credentials match the hardcoded admin, false otherwise.
     */
    public static boolean authenticate(String username, char[] password) {
        String inputPassword = new String(password);

        // Basic check for demo purposes
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(inputPassword);
    }
}