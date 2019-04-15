import java.sql.*;
import java.util.*;

public class DBInterface {

    private static final String DB = "smf"; // Database name
    private static final String USER = "godtUserName"; // Username
    private static final String PASSWORD = "godtPassword"; // Password
    private static final String DB_URL = "jdbc:mysql://mydb.itu.dk/" + DB; // URL for accessing the database

    private Connection connection;

    public static DBInterface dbInterface;

    private DBInterface(){
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            System.out.println("Could not regiser driver. More details: " + e.toString());
        }

    }

    public static DBInterface getDBInterface() {
        if (dbInterface == null){
            dbInterface = new DBInterface();
        }
        return dbInterface;
    }

    public void connect(){
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.toString());
        }
    }

    public void disConnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection to database: " + e.toString());
        }
    }

}