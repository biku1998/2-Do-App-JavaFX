package sample.controller;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.TextFields;
import sample.model.ConnectionProvider;
import sample.model.MainModel;
import sample.model.ServiceProvider;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;


public class LoginController {

    @FXML
    private StackPane rootStackPane;
    @FXML
    private AnchorPane rootPaneLogin;

    @FXML
    private JFXTextField loginUsername;

    @FXML
    private JFXPasswordField loginPassword;

    @FXML
    private JFXButton loginLoginBtn;

    @FXML
    private JFXButton loginSignUpButton;


    @FXML
    void initialize()
    {
        loginSignUpButton.setOnAction(event -> {

            // Sending user to SignUp

            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/sample/fxml/signUp.fxml"));
                rootPaneLogin.getChildren().setAll(pane);
            }
            catch (Exception e)
            {
                ServiceProvider.showException(e);
            }


        });



        loginLoginBtn.setOnAction(event -> {
            String username = loginUsername.getText();
            String password = loginPassword.getText();

            if (username.isEmpty() || password.isEmpty())
            {
                ServiceProvider.showErrorMessage("Empty Fields",rootStackPane,"Something went wrong !!!!");
            }
            else
            {
                if (MainModel.verifyUserFromDB(username.trim(),password.trim()))
                {
                    try {
                        // setting the current user.
                        //setUpCurrentUser(username.trim());
                        String name = getCurrentUserName(username.trim());
                        OtpController.updateCurrentUser(username.trim(),name);
                        //sending the user to the task page.
                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/sample/fxml/Task.fxml"));
//                        AnchorPane pane = FXMLLoader.load(getClass().getResource("/sample/fxml/Task.fxml"));
                        rootPaneLogin.getChildren().setAll(pane);
                    }
                    catch (Exception e)
                    {
                        ServiceProvider.showException(e);
                    }
                }
                else
                {
                    ServiceProvider.showErrorMessage("you are not registered, sign up",rootStackPane,"Something went wrong !!!!");
                }
            }
        });

        // adding autocomplete .

         ArrayList<String> allEmails = getAllEmailList();

        TextFields.bindAutoCompletion(loginUsername,allEmails);

    }

    private ArrayList<String> getAllEmailList() {

        ArrayList<String> usersEmail = new ArrayList<>();

        try {

            Connection connection = ConnectionProvider.getConnection("TODO");
            Statement st = connection.createStatement();

            ResultSet rs = st.executeQuery("select * from user");
            while (rs.next()){
                usersEmail.add(rs.getString("email"));
            }
            return usersEmail;
        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

        return null;
    }

    private String getCurrentUserName(String username) {

        try {
            Connection conn = ConnectionProvider.getConnection("TODO");
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("select * from user where email = '"+username+"'");
            String name = "";
            if (rs.next())
            {
                name  = rs.getString("name");
            }
            return name;
        }
        catch (Exception e){
            ServiceProvider.showException(e);
        }

        return null;
    }


}
