import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class VulnerableApp {

    // Hard-coded password vulnerability
    private static final String DB_PASSWORD = "password123";

    public static void main(String[] args) {
        VulnerableApp app = new VulnerableApp();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (app.login(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed.");
        }

        scanner.close();
    }

    public boolean login(String username, String password) {
        Connection connection = null;
        Statement stmt = null;

        try {
            // Hard-coded database URL and credentials
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", DB_PASSWORD);
            stmt = connection.createStatement();

            // SQL Injection vulnerability
            String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
            ResultSet rs = stmt.executeQuery(query);

            return rs.next();
        } catch (Exception e) {
            // Catching generic exception - SonarQube will flag this as a bad practice
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();

        // No input validation - potential vulnerability
        System.out.println("You entered: " + age);

        if (age > 18) {
            System.out.println("You are an adult.");
        } else {
            System.out.println("You are a minor.");
        }
        scanner.close();
    }
}
