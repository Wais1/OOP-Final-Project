package sample.controller;

import com.jfoenix.controls.JFXButton;
import com.sun.prism.shader.AlphaOne_Color_AlphaTest_Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sample.Database.Const;
import sample.Database.DatabaseHandler;
import sample.animations.Shaker;
import sample.model.Task;
import sample.model.User;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class TodosController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Task> eventList;

    @FXML
    private JFXButton addButton;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField taskNameField;

    @FXML
    private DatePicker datePicker;

    // Declare the user here to access its data later.
    private static User selectedUser;

    // Delcare database handler to create, read, update, delete tasks.
    private DatabaseHandler dataBaseHandler;


    ObservableList<Task> list = FXCollections.observableArrayList();
    @FXML
    void initialize() {
        // Set default value of calendar to today's date.
        datePicker.setValue(LocalDate.now());

        // Initialize database handler
        dataBaseHandler = new DatabaseHandler();

        // Pass user from LoginController into getTasks to access database and return the result.
        ResultSet userRow = dataBaseHandler.getTasks(selectedUser);

        // Initialize counter to count data rows
        int counter = 0;

        try {
            while (userRow.next()) {
                // Increment counter per row
                counter++;
                // Set first name and last name
                String description = userRow.getString(Const.TASKS_DESCRIPTION);
                String taskName = userRow.getString(Const.TASK_NAME);
                Date date = userRow.getDate(Const.TASKS_DATE);

                // Convert date back into original LocalDate object.
                LocalDate datecreated = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

                // Create task object from database to insert onto View.
                Task task = new Task(datecreated, description, taskName);
                // Add task to list.
                addTaskToScreen(task);
                System.out.println(datecreated + ": " + taskName + ": " + description);
            }

            // Validate that result has an item. If true, user exists and show the items screen
            if (counter >= 1) {
                System.out.println("There's task data.");
            } else {
                System.out.println("There's no task data!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };

    // Adds tasks from user input to screen and to database.
    @FXML
    private void addEvent(Event e){
        Task task = new Task(datePicker.getValue(),taskNameField.getText(), descriptionTextField.getText());
        list.add(task);
        eventList.setItems(list);
        // Adds to database
        dataBaseHandler.createTask(selectedUser,task);
        // Empties fields
        refresh();
    }

    // Heloer method: Add data from orignal database tasks onto screen.
    private void addTaskToScreen(Task task)
    {
        list.add(task);
        eventList.setItems(list);

        // Empties fields
        refresh();
    }

    // Helper method: to empty the text fields after a new task is added.
    private void refresh() {
        datePicker.setValue(LocalDate.now());
        taskNameField.setText(null);
        descriptionTextField.setText(null);
    }

    // Receives the user from LoginController, and accesses its data.
    public static void initData(User user){
        selectedUser = user;
        System.out.println("In the todoscontroller, " + user.getFirstName());
    }
}