package sample.controller;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sample.model.ServiceProvider;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskController {


    @FXML
    JFXListView taskList;

    @FXML
    private  AnchorPane TaskRootPane;

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
    private JFXButton TaskLogoutBtn;

    @FXML
    void addTask(ActionEvent event) {
        // adding a new task.
        String currentUser = currentUserLabel.getText().split(" ")[1].trim();

        makeTaskFile(currentUser);

        // clearing the task field.
        newTask.setText("");

        // refreshing the Task Table.
        updateTheTaskList(currentUser);
    }

    private  ArrayList<String> getAllTaskList(String user)
    {
        try
        {

            String currentUser = currentUserLabel.getText().split(" ")[1].trim();
            String pwd = ServiceProvider.getPath();
            String path = pwd+"/src/sample/Data/Tasks/"+currentUser+"Task.txt";

            FileInputStream fis  =  new FileInputStream(path);

            ArrayList<String> tasks =  new ArrayList<>();

            Scanner scanner = new Scanner(fis);

            while(scanner.hasNext())
            {
                tasks.add(scanner.nextLine().trim());
            }

            return tasks;

        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }
        return null;
    }



    @FXML
    void removeTask(ActionEvent event) {

        String removeTask  = taskTORemove.getText().trim();

        // checking if task exist or not
        if (!getAllTaskList(currentUserLabel.getText().split(" ")[1].trim()).contains(removeTask))
        {
            ServiceProvider.showErrorMessage("Task does'nt exist",rootStackPane,"Hii "+currentUserLabel.getText().split(" ")[1].trim());
            return;
        }

        try
        {

        String currentUser = currentUserLabel.getText().split(" ")[1].trim();
        String pwd = ServiceProvider.getPath();
        String path = pwd+"/src/sample/Data/Tasks/"+currentUser+"Task.txt";

        FileInputStream fis  =  new FileInputStream(path);



        ArrayList<String> tasks =  new ArrayList<>();

        Scanner scanner = new Scanner(fis);
        boolean flag = false;
        while(scanner.hasNext())
        {
            flag = true;
            String task = scanner.nextLine().trim();
            if ( ! task.equalsIgnoreCase(removeTask))
            {
                tasks.add(task);
            }
        }

        if (flag)
        {
            String data = "";
            for (String t : tasks)
            {
                data = data + t + "\n";
            }
            // for debug : ServiceProvider.showErrorMessage(data,rootStackPane,"details");
            FileOutputStream fos = new FileOutputStream(path,false);
            fos.write(data.getBytes());
            updateTheTaskList(currentUserLabel.getText().split(" ")[1].trim());

            ServiceProvider.showErrorMessage("Task removed Successfully",rootStackPane,"Hii "+currentUser);

            taskTORemove.setText("");
        }
        else
        {
            ServiceProvider.showErrorMessage("Oops !! Task list is empty ",rootStackPane,"Hii "+currentUser);
        }




    }
        catch (Exception e)
    {
        ServiceProvider.showException(e);
    }

    }

    @FXML
    public void initialize()
    {
        String name = fetchCurrentUser();
        currentUserLabel.setText("Hello "+name);
        updateTheTaskList(name);

        TaskLogoutBtn.setOnAction(actionEvent -> {

            // sending user to login page
            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
                TaskRootPane.getChildren().setAll(pane);
            }
            catch (Exception e)
            {
                ServiceProvider.showException(e);
            }
        });

        // TextFiled autocomplete code.

        textAutoComplete();

        //

        taskList.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                event.consume();
            }
        });
    }

    public void textAutoComplete()
    {
        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.setSelectionHandler(event -> taskTORemove.setText(event.getObject()));
        autoCompletePopup.getSuggestions().addAll(getAllTaskList(currentUserLabel.getText().split(" ")[1].trim()));
        taskTORemove.textProperty().addListener(observable ->{
            autoCompletePopup.filter(s -> s.contains(taskTORemove.getText()));
            if(!autoCompletePopup.getFilteredSuggestions().isEmpty()){
                autoCompletePopup.show(taskTORemove);
            }else{
                autoCompletePopup.hide();
            }
        });
    }

    private String fetchCurrentUser() {

        try
        {

            String pwd = ServiceProvider.getPath();

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


            String pwd = ServiceProvider.getPath();
            String path = pwd+"/src/sample/Data/Tasks/"+currentUser+"Task.txt";

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

            textAutoComplete();


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

            String pwd = ServiceProvider.getPath();
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

            textAutoComplete();

        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

    }
}
