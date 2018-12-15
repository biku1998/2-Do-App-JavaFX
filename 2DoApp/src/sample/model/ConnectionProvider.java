package sample.model;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ConnectionProvider {

    static Connection conn;

    public static Connection getConnection(String db_name)
    {

        ArrayList<String> dbConfig =  ConnectionProvider.getDBConfig();

        String port_no = dbConfig.get(0);

        String url = "jdbc:mysql://localhost:"+port_no+"/"+db_name+"?autoReconnect=true&useSSL=false";
        String usr = dbConfig.get(1);
        String password = dbConfig.get(2);
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

    private static ArrayList<String> getDBConfig() {

        try
        {

            String pwd = ServiceProvider.getPath();

            String path = pwd+"/src/sample/config/configDB.txt";

            FileInputStream fis = new FileInputStream(path);

            Scanner s = new Scanner(fis);

            ArrayList<String> dbConfig = new ArrayList<>();

            String [] d = {};

            while(s.hasNext())
            {
                 d = s.nextLine().split(":");
                 dbConfig.add(d[1].trim());

            }

            return dbConfig;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            //ServiceProvider.showException(e);
        }

        return null;
    }
}
