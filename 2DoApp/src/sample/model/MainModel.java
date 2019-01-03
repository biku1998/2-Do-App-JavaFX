package sample.model;
import sample.user.User;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class MainModel {

    public static boolean sendDataToDb(User user)
    {
        try
        {
            Connection conn = ConnectionProvider.getConnection("TODO");

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

    public static boolean verifyUserFromDB(String email , String password)
    {
        try
        {
            Connection conn  = ConnectionProvider.getConnection("TODO");
            Statement st   = conn.createStatement();

            ResultSet rs  = st.executeQuery("select * from user where email  = '"+email+"' and password = '"+password+"'");

            if (rs.next()){
                return true;
            }
        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }
        return false;
    }


}
