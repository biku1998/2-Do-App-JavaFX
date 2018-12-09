package sample.controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane signUpRootPane;

    @FXML
    private TextField username;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    void checkData(ActionEvent event) {

        String name = username.getText();

        String mail = email.getText();

        String passwd = password.getText();

        if(name.equals("") || mail.equals("") || passwd.equals(""))
        {
           ServiceProvider.showErrorMessage("Ooops, You have left an empty field!");
        }
        else
        {


            if(!ServiceProvider.emailValidation(mail))
            {
                ServiceProvider.showErrorMessage("email not valid");
            }
            else if(!ServiceProvider.passwordValidation(passwd)) {
                ServiceProvider.showErrorMessage("password too short");
            }
            else
            {
                userToUpdate.add(name);userToUpdate.add(mail);userToUpdate.add(passwd);

                OtpController.verifyOtp(mail);

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
        MainModel.sendDataToDb(u);
    }

}
