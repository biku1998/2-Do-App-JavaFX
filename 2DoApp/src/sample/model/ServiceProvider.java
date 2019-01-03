package sample.model;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ServiceProvider {

    public static  void showErrorMessage(String msg, StackPane pane,String title)
    {

        //String title  = "Something went wrong !!!!";
        JFXDialogLayout dialogContent = new JFXDialogLayout();
        dialogContent.setHeading(new Text(title));
        dialogContent.setBody(new Text(msg));

        JFXButton close = new JFXButton("close");
        close.setButtonType(JFXButton.ButtonType.RAISED);
        close.setStyle("-fx-background-color : #e91e63;-fx-text-fill: #ffffff;");
        //close.setStyle("-fx-text-fill: #ffffff");

        dialogContent.setActions(close);

        JFXDialog dialog = new JFXDialog(pane,dialogContent,JFXDialog.DialogTransition.RIGHT);

        close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                dialog.close();
            }
        });
        dialog.show();

        //

    }


    public static ArrayList<String> getAllEmails()
    {

        try
        {

            String pwd = getPath();
            String path = pwd+"/src/sample/Data/user.txt";
            FileInputStream fis = new FileInputStream(path);

            Scanner scanner = new Scanner(fis);

            ArrayList<String> emails = new ArrayList<>();

            while(scanner.hasNext())
            {
                String identifier = scanner.nextLine();
                String nameData = scanner.nextLine().split(":")[1];
                String emailData = scanner.nextLine().split(":")[1];
                String passwordData = scanner.nextLine().split(":")[1];

                emails.add(emailData);
            }

            return emails;
        }
        catch (Exception e)
        {
            showException(e);
        }

        return null;
    }

    public static void showMessage(String msg)
    {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("");
        alert.setContentText(msg);
        alert.showAndWait();

    }


    public static void  showException( Exception ex )
    {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText("Something went wrong");



        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    public static boolean emailValidation(String email)
    {

        if (email.contains("@") && (email.contains(".com") || email.contains(".in")))
        {
            return true;
        }

        return false;
    }

    public static boolean passwordValidation(String password)
    {

        if (password.length() >= 5)
        {
            return true;
        }

        return false;
    }


    public static String sendOtp(String email)
    {
        Random rnd = new Random();
        String otp = String.valueOf(100000 + rnd.nextInt(900000));

        EmailServer.sendMail(email,otp);

        return otp;
    }

    public static String getPath()
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

            return pwd;
        }
        catch (Exception e)
        {
            showException(e);
        }

        return null;
    }

    public static void createFileForTask(String username)
    {
        try
        {
            String pwd = ServiceProvider.getPath();
            String path = pwd+"/src/sample/Data/Tasks/"+username+"Task.txt";

            FileOutputStream fos = new FileOutputStream(path,true);

        }
        catch (Exception e)
        {
            showException(e);
        }
    }



}
