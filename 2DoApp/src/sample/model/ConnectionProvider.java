package sample.model;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectionProvider {

    static Connection conn;

    public static Connection getConnection(String db_name)
    {

       // ArrayList<String> dbConfig =  ConnectionProvider.getDBConfig();

        String port_no = "3306"; // your mysql port no here.

        String url = "jdbc:mysql://localhost:"+port_no+"/"+db_name+"?autoReconnect=true&useSSL=false";
        String usr = "root"; // your username for mysql.
        String password = "root"; // your password for sql.
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
