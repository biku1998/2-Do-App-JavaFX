package sample.model;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class ServiceProvider {

    public static  void showErrorMessage(String msg)
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
        int n = 100000 + rnd.nextInt(900000);

        String otpp = "OTP::"+n+"::";

        String subject = "ConfirmYourEmail";

        String pwd = "";
        try
        {
            String [] cmd = {"pwd"};
            ProcessBuilder pb = new ProcessBuilder(cmd);

            Process p = pb.start();

            OutputStream os = p.getOutputStream();

            PrintStream ps = new PrintStream(os);

            Scanner sc = new Scanner(new InputStreamReader(p.getInputStream()));

            pwd = sc.nextLine();

            //showErrorMessage(pwd);

        }
        catch (Exception e)
        {
            showException(e);
        }



        String p = String.format("python3  %s/src/sample/api/sendMail.py",pwd);
        String cmd = p+" "+ subject + "  " + otpp + "  " + email;

        System.out.println(cmd);
        try
        {
            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

        return String.valueOf(n);

    }

}
