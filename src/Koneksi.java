import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {

    public static void main(String[] args) {

        try {

            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_kasir_dev",
                "root",
                ""
            );

            System.out.println("Koneksi berhasil");

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }
}
