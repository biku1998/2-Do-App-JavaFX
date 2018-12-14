package sample.model;

import sample.user.User;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class MainModel {

    public static boolean sendDataToDb(User user)
    {
        try
        {
            Connection conn = ConnectionProvider.getConnection("todo");

            Statement st = conn.createStatement();

            String sql = String.format("insert into user(email,name,password) values('%s','%s','%s')",user.getEmail(),user.getName(),user
            .getPassword());

            st.execute(sql);

            return true;

        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

        return false;
    }

    public static boolean verifyUser(String email , String password)
    {
        try
        {
            Connection conn =ConnectionProvider.getConnection("todo");

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("select * from user where email = '"+email+"' and password = '"+password+"'");

            if (rs.next())
            {
                return true;
            }

        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }



        return false;
    }

    // this method will write data to file database.
    public static void sendDataTOFileDB(User user)
    {
        try
        {
            // getting the path.
            String [] cmd = {"pwd"};
            ProcessBuilder pb = new ProcessBuilder(cmd);

            Process p = pb.start();

            OutputStream os = p.getOutputStream();

            PrintStream ps = new PrintStream(os);

            Scanner sc = new Scanner(new InputStreamReader(p.getInputStream()));

            String pwd = sc.nextLine();

            // making a file object.

            // sample path :  /home/sourabh/Desktop/Workspace/Intellij-ws/2-Do-App-JavaFX/2DoApp

            FileOutputStream fos = new FileOutputStream(pwd+"/src/sample/Data/user.txt",true);

            String data = String.format("!\nname : %s\nemail : %s\npassword : %s\n",user.getName(),
                    user.getEmail(),user.getPassword());

            fos.write(data.getBytes());




        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }
    }

    public static boolean verifyUserFromFileDB(String email , String password)
    {
        try
        {
            String [] cmd = {"pwd"};
            ProcessBuilder pb = new ProcessBuilder(cmd);

            Process p = pb.start();

            OutputStream os = p.getOutputStream();

            PrintStream ps = new PrintStream(os);

            Scanner sc = new Scanner(new InputStreamReader(p.getInputStream()));

            String pwd = sc.nextLine();
            String path = pwd+"/src/sample/Data/user.txt";
            FileInputStream fis = new FileInputStream(path);

            Scanner scanner = new Scanner(fis);

            while(scanner.hasNext())
            {
                String identifier = scanner.nextLine();
                String nameData = scanner.nextLine().split(":")[1];
                String emailData = scanner.nextLine().split(":")[1];
                String passwordData = scanner.nextLine().split(":")[1];

                if(email.equalsIgnoreCase(emailData.trim()) && password.equalsIgnoreCase(passwordData.trim()))
                {
                    return true;
                }
            }

        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }


        return false;
    }


}
