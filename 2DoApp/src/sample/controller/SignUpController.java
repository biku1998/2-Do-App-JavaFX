package sample.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.model.MainModel;
import sample.model.ServiceProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.user.User;

import java.util.ArrayList;


public class SignUpController {

    private static ArrayList<String> userToUpdate = new ArrayList<>();

    @FXML
    private StackPane rootStackPane;

    @FXML
    private AnchorPane signUpRootPane;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton signUpLoginbtn;


    @FXML
    void initialize()
    {
        signUpLoginbtn.setOnAction(event -> {

            // sending user to login page
            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
                signUpRootPane.getChildren().setAll(pane);
            }
            catch (Exception e)
            {
                ServiceProvider.showException(e);
            }
        });
    }




    @FXML
    void checkData(ActionEvent event) {

        String name = username.getText();

        String mail = email.getText();

        String passwd = password.getText();

        if(name.equals("") || mail.equals("") || passwd.equals(""))
        {
           ServiceProvider.showErrorMessage("Ooops, You have left an empty field!",rootStackPane,"Something went wrong !!!!");
        }
        else
        {


            if(!ServiceProvider.emailValidation(mail))
            {
                ServiceProvider.showErrorMessage("email not valid",rootStackPane,"Something went wrong !!!!");
            }
            else if(!ServiceProvider.passwordValidation(passwd)) {
                ServiceProvider.showErrorMessage("password too short",rootStackPane,"Something went wrong !!!!");
            }
            else
            {
                // code for duplicate or subset names and name containing spaces.
                if (name.contains(" "))
                {
                    name = name.replace(" ","~");
                }
                userToUpdate.add(name);userToUpdate.add(mail);userToUpdate.add(passwd);


                // first checking if the user already exist or not.
                if(MainModel.verifyUserFromFileDB(mail,passwd))
                {
                    ServiceProvider.showMessage("email already registered , please login.");
                    return;
                }



                OtpController.verifyOtp(mail);

                OtpController.setMail(mail);

                // TODO : write code to change the scene to otp Screen.



                try
                {
                    AnchorPane pane = FXMLLoader.load(getClass().getResource("../fxml/OtpConfirm.fxml"));
                    signUpRootPane.getChildren().setAll(pane);
                }
                catch (Exception e)
                {
                    ServiceProvider.showException(e);
                }


            }
        }

    }


    public  static void insertUser()
    {
        User u = new User(userToUpdate.get(0),userToUpdate.get(1),userToUpdate.get(2));
        //MainModel.sendDataToDb(u);
        MainModel.sendDataTOFileDB(u);
        ServiceProvider.createFileForTask(u.getName());
    }

}
