package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.model.MainModel;
import sample.model.ServiceProvider;


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
                ServiceProvider.showErrorMessage("Empty Fields",rootStackPane);
            }
            else
            {
                if (MainModel.verifyUser(username,password))
                {
                    try {
                        // after authentication sending the user to the task page.
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
                    ServiceProvider.showErrorMessage("you are not registered, sign up",rootStackPane);
                }
            }
        });
    }
}
