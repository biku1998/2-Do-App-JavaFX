package sample.controller;

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
                if (MainModel.verifyUserFromFileDB(username,password))
                {
                    try {
                        // setting the current user.
                        setUpCurrentUser(username);

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
    }

    private void setUpCurrentUser(String email)
    {
        try
        {
            String [] cmd = {"pwd"};
            ProcessBuilder pb = new ProcessBuilder(cmd);

            Process p = pb.start();

            OutputStream os = p.getOutputStream();

            PrintStream ps = new PrintStream(os);

            Scanner sc = new Scanner(new InputStreamReader(p.getInputStream()));

            String pwd = sc.nextLine();

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
//
//                System.out.println(identifier);
//                System.out.println(nameData);
//                System.out.println(emailData);
//                System.out.println(passwordData);

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
