import java.sql.SQLException;

public class Main {
    static SQLite sqlite;
    public static void main(String[] args) throws SQLException {
        sqlite = new SQLite();
        GUI loginGUI = new GUI();
    }
}
