package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.model.ServiceProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class OtpController {

    static ArrayList<String> otpData  = new ArrayList();

    public static String Email;

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

                   updateCurrentUser(Email);

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

    private void updateCurrentUser(String email) {
        try
        {


            String pwd = ServiceProvider.getPath();

            String path = pwd+"/src/sample/Data/currentUser.txt";



            FileOutputStream fos = new FileOutputStream(path,false);

            //fos.write(email.getBytes());

            FileInputStream fis = new FileInputStream(pwd+"/src/sample/Data/user.txt");

            Scanner scanner = new Scanner(fis);

            String nameData = "";

            while(scanner.hasNext())
            {
                String identifier = scanner.nextLine().trim();
                nameData = scanner.nextLine().split(":")[1].trim();
                String emailData = scanner.nextLine().split(":")[1].trim();
                String passwordData = scanner.nextLine().split(":")[1].trim();


                if (email.equalsIgnoreCase(emailData))
                {
                    String data = String.format("!\nname : %s\nemail : %s\npassword : %s\n",nameData,
                            emailData,passwordData);
                    fos.write(data.getBytes());
                }

            }

            String path2 = pwd+"/src/sample/Data/Tasks/"+nameData+"Task.txt";

            FileOutputStream fos2 = new FileOutputStream(path2,true);



        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

    }

    public static void  verifyOtp(String email)
    {

        String otpSent = ServiceProvider.sendOtp(email);

        otpData.add(otpSent);
    }


}
