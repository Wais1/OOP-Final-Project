package sample.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Database.DatabaseHandler;
import sample.animations.Shaker;
import sample.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton loginSignupButton;

    private DatabaseHandler dataBaseHandler;

    @FXML
    void initialize() {

        dataBaseHandler = new DatabaseHandler();

        loginButton.setOnAction(event -> {

            // Trim the input from loginUsername and loginPassword text fields
            String loginText = loginUsername.getText().trim();
            String loginPwd = loginPassword.getText().trim();

            // Create a new User object
            User user = new User();
            user.setUserName(loginText);
            user.setPassword(loginPwd);

            // Pass new user object with entered info into getUser to access database and return the result.
            ResultSet userRow = dataBaseHandler.getUser(user);

            // Initialize counter to count data rows
            int counter = 0;

            try {
                while (userRow.next()) {
                    // Increment counter per row
                    counter++;
                    // Set first name and last name
                    String fName = userRow.getString("firstname");
                    user.setFirstName(fName);

                    String lName = userRow.getString("lastname");
                    user.setLastName(lName);

                    // Set uid for later access
                    int uid = userRow.getInt("userid");
                    user.setUid(uid);

                    System.out.println("Welcome! " + fName + " " + uid);
                }

                // Validate that result has an item. If true, user exists and show the items screen
                if (counter >= 1) {
                    // Pass the user into TodosController through initializing it there.
                    TodosController.initData(user);

                    // Show item screen
                    showAddItemScreen();
                } else {
                    // Creates a custom animation for the username and password to show error.
                    Shaker userNameShaker = new Shaker(loginUsername);
                    Shaker passwordShaker = new Shaker(loginPassword);
                    passwordShaker.shake();
                    userNameShaker.shake();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        loginSignupButton.setOnAction(event -> {
            // Take users to signup screen
            loginSignupButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/view/signup.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

        });
    }

    private void showAddItemScreen() {
        // Take users to AddItem screen
        loginSignupButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/view/todos.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}