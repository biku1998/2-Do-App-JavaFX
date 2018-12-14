package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import sample.model.ServiceProvider;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskController {


    @FXML
    JFXListView taskList;

    @FXML
    private StackPane rootStackPane;

    @FXML
    private JFXButton addTaskButton;

    @FXML
    private JFXTextField newTask;

    @FXML
    private JFXTextField taskTORemove;

    @FXML
    private JFXButton removeTaskButton;

    @FXML
    private Label currentUserLabel;

    @FXML
    void addTask(ActionEvent event) {
        // adding a new task.
        String currentUser = currentUserLabel.getText().split(" ")[1].trim();

        // creating user task file and adding  the task.
        makeTaskFile(currentUser);

        // clearing the task field.
        newTask.setText("");



        // refreshing the Task Table.
        updateTheTaskList(currentUser);
    }




    @FXML
    void removeTask(ActionEvent event) {

    }

    @FXML
    public void initialize()
    {
        String name = fetchCurrentUser();
        currentUserLabel.setText("Hello "+name);
        updateTheTaskList(name);
    }

    private String fetchCurrentUser() {

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
            FileInputStream fis = new FileInputStream(path);
            Scanner scanner = new Scanner(fis);



            while (scanner.hasNext())
            {
                String id = scanner.nextLine().trim();
                String nameData = scanner.nextLine().split(":")[1].trim();
                String emailData = scanner.nextLine().split(":")[1].trim();
                String passwordData = scanner.nextLine().split(":")[1].trim();

                return nameData;

            }

        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

        return null;
    }

    private void makeTaskFile(String currentUser) {
        // this function will create a task file for user provided.
        try
        {

            String [] cmd = {"pwd"};
            ProcessBuilder pb = new ProcessBuilder(cmd);

            Process p = pb.start();

            OutputStream os = p.getOutputStream();

            PrintStream ps = new PrintStream(os);

            Scanner sc = new Scanner(new InputStreamReader(p.getInputStream()));

            String pwd = sc.nextLine();
            String path = pwd+"/src/sample/Data/Tasks/"+currentUser+"Task.txt";
//
//            Runtime rt = Runtime.getRuntime();
//
//            rt.exec("cd "+path+" touch "+currentUser+"Task.txt");
            FileOutputStream fos = new FileOutputStream(path,true);

            String t = newTask.getText();

            if (t.equals(""))
            {
                ServiceProvider.showErrorMessage("please add task",rootStackPane,"Empty fields");
                return;
            }

            String Task = String.format("%s\n",t);

            fos.write(Task.getBytes());

            // giving notification.

            ServiceProvider.showErrorMessage("Task added Successfully",rootStackPane,"Hii "+currentUser);


        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }


    }

    // for updating the Task list.
    private void updateTheTaskList(String currentUser) {
        taskList.getItems().clear();
        try
        {
            String [] cmd = {"pwd"};
            ProcessBuilder pb = new ProcessBuilder(cmd);

            Process p = pb.start();

            OutputStream os = p.getOutputStream();

            PrintStream ps = new PrintStream(os);

            Scanner sc = new Scanner(new InputStreamReader(p.getInputStream()));

            String pwd = sc.nextLine();
            String path = pwd+"/src/sample/Data/Tasks/"+currentUser+"Task.txt";

            FileInputStream fis = new FileInputStream(path);

            ArrayList<String> tasks = new ArrayList<>();

            Scanner scanner = new Scanner(fis);

            while(scanner.hasNext())
            {
                String task = scanner.nextLine().trim();
                tasks.add(task);
            }
            int sno = 1;
            for (String t:tasks) {
                taskList.getItems().add(new Label(sno +"  :  "+ t));
                sno++;
            }



        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

    }
}
