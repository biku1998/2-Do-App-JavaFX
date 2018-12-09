package sample.model;

import sample.user.User;

import java.sql.Connection;
import java.sql.Statement;

public class MainModel {

    public static boolean sendDataToDb(User user)
    {
        try
        {
            Connection conn = ConnectionProvider.getConnection("todo","3306");

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

}
