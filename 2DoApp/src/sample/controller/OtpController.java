package sample.controller;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.model.ConnectionProvider;
import sample.model.ServiceProvider;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;



public class OtpController {

    static ArrayList<String> otpData  = new ArrayList();

    public static String Email;
    public static String Name;

    public static  void setName(String name)
    {
        OtpController.Name = name;
    }

    public static  void setMail(String email)
    {
        OtpController.Email = email;
    }

    @FXML
    private AnchorPane rootPaneOtp;

    @FXML
    private StackPane rootStackPane;

    @FXML
    private TextField otpProvided;

    @FXML
    public void verifyOtp(ActionEvent e)
    {
       String otp = otpProvided.getText();
       if (otp.isEmpty())
       {
           ServiceProvider.showErrorMessage("Please enter otp",rootStackPane,"Something went wrong !!!!");
       }
       else
       {

           String otpSent = otpData.get(0);

           if(otp.equals(otpSent))
           {
               // entering user to database.

               SignUpController.insertUser();

               // TODO : write code to send the user to task manage screen.

               try {
                   // updating the current user.

                   updateCurrentUser(Email,Name);

                   // after authentication sending the user to the task page.
                   AnchorPane pane = FXMLLoader.load(getClass().getResource("/sample/fxml/Task.fxml"));
                   rootPaneOtp.getChildren().setAll(pane);
               }
               catch (Exception g)
               {
                   ServiceProvider.showException(g);
               }
           }
           else
           {
               ServiceProvider.showErrorMessage("invalid otp",rootStackPane,"Something went wrong !!!!");
           }

       }
    }

    public static void updateCurrentUser(String email,String name) {
        try
        {
            // adding current user to DB.

            Connection conn  = ConnectionProvider.getConnection("TODO");

            Statement st = conn.createStatement();

            st.execute("DELETE FROM currentUser");

            String sql  = String.format("insert into currentUser(email,name)values('%s','%s')",email,name);

            st.execute(sql);

        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

    }

    public static void  sendAndSaveOTP(String email)
    {
        String otpSent = ServiceProvider.sendOtp(email);
        //System.out.println(otpSent);
        otpData.add(otpSent);
    }


}
