import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mariadb://localhost:3306/rubrica_db";
        String username = "root";
        String password = "newpassword";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connesso a MariaDB con successo!");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Errore nella connessione a MariaDB.");
            e.printStackTrace();
        }
    }
}
