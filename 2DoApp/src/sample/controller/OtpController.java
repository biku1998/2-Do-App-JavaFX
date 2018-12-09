package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.model.ServiceProvider;

import java.util.ArrayList;


public class OtpController {

    static ArrayList<String> otpData  = new ArrayList();


    //Test code
//    public OtpController() {
//
//        verifyOtp("kumarsourabh228edu@gmail.com");
//    }


    @FXML
    private TextField otpProvided;

    @FXML
    public void verifyOtp(ActionEvent e)
    {
       String otp = otpProvided.getText();
       if (otp.isEmpty())
       {
           ServiceProvider.showErrorMessage("Please enter otp");
       }
       else
       {

           String otpSent = otpData.get(0);

           if(otp.equals(otpSent))
           {
               // entering user to database.

               SignUpController.insertUser();

               // TODO : write code to send the user to task manage screen.
           }
           else
           {
               ServiceProvider.showErrorMessage("invalid otp");
           }

       }
    }

    public static void  verifyOtp(String email)
    {

        String otpSent = ServiceProvider.sendOtp(email);

        otpData.add(otpSent);
    }


}
