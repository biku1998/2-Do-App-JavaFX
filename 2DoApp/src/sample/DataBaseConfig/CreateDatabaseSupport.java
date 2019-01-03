package sample.DataBaseConfig;
import sample.model.ServiceProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDatabaseSupport {
    public static void main(String[] args) {
        try {

            String port_no = "3306"; // your mysql port no here.
            String url = "jdbc:mysql://localhost:"+port_no+"/"+"information_schema"+"?autoReconnect=true&useSSL=false";
            String usr = "root"; // your username for mysql.
            String password = "root"; // your password for sql.
            Connection connection = DriverManager.getConnection(url,usr,password);
            Statement st = connection.createStatement();
            String sql1 = "create database TODO";
            String sql2 = "use TODO";
            String sql3 = "create table user(email varchar(100) primary key,name varchar(100) not null,password varchar(100) not null)";
            String sql4 = "create table task(email varchar(100) not null, task varchar(500) not null, foreign key(email) references user(email))";
            String sql5 = "create table currentUser(email varchar(100) not null,name varchar(100) not null, foreign key(email) references user(email))";

            st.execute(sql1); st.execute(sql2); st.execute(sql3); st.execute(sql4); st.execute(sql5);

            System.out.println("All the necessary DataBase created");


        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }
    }
}
