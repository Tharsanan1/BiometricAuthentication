import java.sql.*;

public class SQLite {
    Connection con;
    SQLite() throws SQLException {
        con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        createTable();
    }
    public void createTable() throws SQLException {
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='keystroke_user'");
        if(!res.next()){
            Statement statement1 = con.createStatement();
            statement1.execute("CREATE TABLE keystroke_user(id integer,name varchar(100),password varchar(100),flyGrid varchar(5000),stayGrid varchar(5000),primary key(id));");
            statement1.close();
        }
    }
    public void insertUser(String name, String password, String flyGrid, String stayGrid) throws SQLException{
        Statement statement = con.createStatement();
        statement.executeUpdate("INSERT INTO keystroke_user (name,password,flyGrid,stayGrid) VALUES('"+name+"','"+password+"','"+flyGrid+"','"+stayGrid+"');");
        statement.close();
    }
    public boolean checkUser(String name) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("SELECT name From keystroke_user WHERE name ='"+name+"';");
        if(res.next()){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean checkAuthentication(String name, String password) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("SELECT name From keystroke_user WHERE name ='"+name+"' AND password = '"+password+"';");
        if(res.next()){
            return true;
        }
        else {
            return false;
        }
    }
    public String getFlyGrid(String name, String password) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("SELECT flyGrid From keystroke_user WHERE name ='"+name+"' AND password = '"+password+"';");
        if(res.next()){
            int len = res.getString("flyGrid").split(" ").length;
            System.out.println("Length of the flygrid is : "+len);
            return res.getString("flyGrid");
        }
        else {
            return "not found";
        }
    }
    public String getStayGrid(String name, String password) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet res = statement.executeQuery("SELECT stayGrid From keystroke_user WHERE name ='"+name+"' AND password = '"+password+"';");
        if(res.next()){
            int len = res.getString("stayGrid").split(" ").length;
            System.out.println("Length of the stayGrid is : "+len);
            return res.getString("stayGrid");
        }
        else {
            return "not found";
        }
    }
}
