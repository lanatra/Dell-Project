package DataLayer;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static String driver = "oracle.jdbc.driver.OracleDriver";
    private static String URL = "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat";
    private static String id = "cphlj222";	//Insert Your ORACLE id and password
    private static String pw = "cphlj222";

    private Connection con;

    //-- Singleton ----
    private static DatabaseConnection instance;

    private DatabaseConnection()
    {
        try
        {
            Class.forName(driver);          // necessary in GlassFish server
            con = DriverManager.getConnection(URL, id, pw);
        } catch (Exception e)
        {
            System.out.println("\n*** Remember to insert your Oracle ID and PW in the DBConnector class! ***\n");
            System.out.println("error in DBConnector.getConnection()");
            System.out.println(e);
        }
    }
    public static DatabaseConnection getInstance()
    {
        if (instance == null)
            instance = new DatabaseConnection();
        return instance;
    }
    //------------------

    public Connection getConnection()
    {
        return con;
    }

}
