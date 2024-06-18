package helpers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

        private static Connection connection1;


        public static void connect() {
            try {
                String url1 = "jdbc:h2:~/play";

                connection1 = DriverManager.getConnection(url1);

                System.out.println("Connection to H2 database has been established.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        public static Connection getConnection1() {
            if (connection1 == null) {
                connect();
            }
            return connection1;
        }



}
