import java.sql.Connection;
import java.sql.DriverManager;

public class TestJDBC {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bank", "root", "ball1colour"
            );
            System.out.println("✅ Connected to MySQL!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
