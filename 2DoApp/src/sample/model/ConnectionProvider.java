package sample.model;
import java.sql.*;

public class ConnectionProvider {

    static Connection conn;

    public static Connection getConnection(String db_name, String port_no)
    {
        String url = "jdbc:mysql://localhost:"+port_no+"/"+db_name+"?autoReconnect=true&useSSL=false";
        String usr = "root";

        //String password = "root"; // for my linux
        String password = "rootuser"; // for mac
        try
        {
            conn = DriverManager.getConnection(url,usr,password);
            return conn;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

       return null;
    }
}
