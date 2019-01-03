package sample.controller;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.TextFields;
import sample.model.ConnectionProvider;
import sample.model.ServiceProvider;


import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

        String t  = newTask.getText().trim();

        if (t.equals(""))
        {
            ServiceProvider.showErrorMessage("please add task",rootStackPane,"Empty fields");
            return;
        }

        if (getAllTaskList(fetchCurrentUser()[1]).contains(t)){
            ServiceProvider.showErrorMessage("Task already added !!",rootStackPane,"Hello "+fetchCurrentUser()[0]);
            return;
        }
        //makeTaskFile(currentUser);

        // adding task to the DB.

       try {

           Connection conn = ConnectionProvider.getConnection("TODO");

           Statement st = conn.createStatement();

           // first get all the task if present.

           String prevTasks  = "";

           ResultSet rs  =  st.executeQuery("select * from task where email  = '"+fetchCurrentUser()[1]+"'");

           if (rs.next()){
               prevTasks = rs.getString("task");
               prevTasks = prevTasks+","+t;
               st.execute("update  task set task = '"+prevTasks+"' where email = '"+fetchCurrentUser()[1]+"' ");
           }
           else{
               st.execute("insert into task(email,task) values('"+fetchCurrentUser()[1]+"','"+t+"')");
           }

       }catch (Exception e){
           ServiceProvider.showException(e);
       }
        // clearing the task field.
        newTask.setText("");
        // refreshing the Task Table.
        updateTheTaskList(fetchCurrentUser()[1]);
        addAutoCompleteToTask();
    }

    private void addAutoCompleteToTask(){
        ArrayList<String> allTask = getAllTaskList(fetchCurrentUser()[1]);
        TextFields.bindAutoCompletion(taskTORemove,allTask);
    }

    private  ArrayList<String> getAllTaskList(String email)
    {
        try
        {
            ArrayList<String> tasks =  new ArrayList<>();

            Connection conn = ConnectionProvider.getConnection("TODO");

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("select * from task where email = '"+email+"' ");

            String [] t = {};
            while (rs.next()){
                t = rs.getString("task").split(",");
            }

            for (String i : t) {
                tasks.add(i);
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

        String removeT  = taskTORemove.getText().trim();

        if (removeT.isEmpty()){
            ServiceProvider.showErrorMessage("Empty task field !!",rootStackPane,"OOPS !!");
            return;
        }

        String info  [] = fetchCurrentUser();
        // checking if task exist or not
        if (!(getAllTaskList(info[1])).contains(removeT))
        {
            ServiceProvider.showErrorMessage("Task does'nt exist",rootStackPane,"Hii "+currentUserLabel.getText().split(" ")[1].trim());
            return;
        }

        try
        {
            Connection connection = ConnectionProvider.getConnection("TODO");

            Statement st = connection.createStatement();


            // code for removing the task from DB and Update View.

            ArrayList<String> taskList  = getAllTaskList(info[1]);

            // removing the task from task list.

            taskList.remove(removeT);

            // updating the DB.

            String taskData ="";

            for (String t:
                 taskList) {
                taskData = taskData +t+",";
            }

            String sql  = "update task set task = '"+taskData+"' where email = '"+info[1]+"'";
            st.execute(sql);

            updateTheTaskList(info[1]);

            ServiceProvider.showErrorMessage("Task removed Successfully",rootStackPane,"Hii "+info[0]);
            taskTORemove.setText("");

        }
        catch (Exception e){
            ServiceProvider.showException(e);
        }
        addAutoCompleteToTask();
    }

    @FXML
    public void initialize()
    {
        String [] info = fetchCurrentUser();
        // checking for current user. if there is not current user then setting the view to login.
        if (info[0] == ""){
            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/sample/fxml/login.fxml"));
                TaskRootPane.getChildren().setAll(pane);
                return;
            }
            catch (Exception e)
            {
                ServiceProvider.showException(e);
            }
        }
        currentUserLabel.setText("Hello "+info[0]);
        updateTheTaskList(info[1]);

        TaskLogoutBtn.setOnAction(actionEvent -> {

            // sending user to login page
            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/sample/fxml/login.fxml"));
                TaskRootPane.getChildren().setAll(pane);
            }
            catch (Exception e)
            {
                ServiceProvider.showException(e);
            }
        });

        // TextFiled autocomplete code.

       // textAutoComplete();

        //

        taskList.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                event.consume();
            }
        });
        addAutoCompleteToTask();
    }

    private String[] fetchCurrentUser() {

        try
        {

            //fetching current user from DB.

            Connection conn = ConnectionProvider.getConnection("TODO");

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("select * from currentUser");

            String email = "";
            String name = "";

            while(rs.next()){
                email = rs.getString("email");
                name = rs.getString("name");
            }

            String [] info = {name,email};

            return info;


        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

        return null;
    }


    // for updating the Task list.
    private void updateTheTaskList(String email) {
        taskList.getItems().clear();
        try
        {
            // adding task for user.

            String [] tasks = {};

            Connection conn = ConnectionProvider.getConnection("TODO");

            Statement st = conn.createStatement();

            ResultSet rs  = st.executeQuery("select *from Task where email = '"+email+"' ");

            while(rs.next()){
                tasks = rs.getString("task").split(",");
            }

            int sno = 1;
            for (String t:tasks) {
                if (!t.isEmpty()) {
                    taskList.getItems().add(new Label(sno + "  :  " + t));
                    sno++;
                }
            }
        }
        catch (Exception e)
        {
            ServiceProvider.showException(e);
        }

    }
}
