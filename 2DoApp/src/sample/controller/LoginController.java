package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.model.MainModel;
import sample.model.ServiceProvider;


public class LoginController {


    @FXML
    private AnchorPane rootPaneLogin;

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Button loginLoginBtn;

    @FXML
    private Button loginSignUpButton;


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
                ServiceProvider.showErrorMessage("Empty Fields");
            }
            else
            {
                if (MainModel.verifyUser(username,password))
                {
                    try {
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
                    ServiceProvider.showErrorMessage("you are not registered, sign up");
                }
            }
        });
    }
}
