package sample.controller;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.model.MainModel;
import sample.model.ServiceProvider;

import java.io.*;
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
                AnchorPane pane = FXMLLoader.load(getClass().getResource("../fxml/signUp.fxml"));
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
                if (MainModel.verifyUserFromFileDB(username.trim(),password.trim()))
                {
                    try {
                        // setting the current user.
                        setUpCurrentUser(username.trim());

                        //sending the user to the task page.
                        AnchorPane pane = FXMLLoader.load(getClass().getResource("../fxml/Task.fxml"));
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


        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        //autoCompletePopup.setStyle("-fx-background-color : #e91e63;");
        autoCompletePopup.setSelectionHandler(event -> loginUsername.setText(event.getObject()));
        autoCompletePopup.getSuggestions().addAll(ServiceProvider.getAllEmails());
        loginUsername.textProperty().addListener(observable ->{
            autoCompletePopup.filter(s -> s.contains(loginUsername.getText()));
            if(!autoCompletePopup.getFilteredSuggestions().isEmpty()){
                autoCompletePopup.show(loginUsername);
            }else{
                autoCompletePopup.hide();
            }
        });


    }

    private void setUpCurrentUser(String email)
    {
        try
        {

            String pwd = ServiceProvider.getPath();

            String path = pwd+"/src/sample/Data/currentUser.txt";

            FileOutputStream fos = new FileOutputStream(path,false);

            //fos.write(email.getBytes());

            FileInputStream fis = new FileInputStream(pwd+"/src/sample/Data/user.txt");

            Scanner scanner = new Scanner(fis);

            while(scanner.hasNext())
            {
                String identifier = scanner.nextLine().trim();
                String nameData = scanner.nextLine().split(":")[1].trim();
                String emailData = scanner.nextLine().split(":")[1].trim();
                String passwordData = scanner.nextLine().split(":")[1].trim();


                if (email.equalsIgnoreCase(emailData))
                {
                    String data = String.format("!\nname : %s\nemail : %s\npassword : %s\n",nameData,
                            emailData,passwordData);
                    fos.write(data.getBytes());
                }

            }



        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }
    }

}
