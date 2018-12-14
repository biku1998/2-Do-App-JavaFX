package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.model.ServiceProvider;

import java.util.ArrayList;


public class OtpController {

    static ArrayList<String> otpData  = new ArrayList();


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

                   // after authentication sending the user to the task page.
                   AnchorPane pane = FXMLLoader.load(getClass().getResource("../fxml/Task.fxml"));
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

    public static void  verifyOtp(String email)
    {

        String otpSent = ServiceProvider.sendOtp(email);

        otpData.add(otpSent);
    }


}
