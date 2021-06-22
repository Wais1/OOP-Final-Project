package sample.Database;


import sample.model.Task;
import sample.model.User;

import java.sql.*;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/"
                + dbName;
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,dbUser,dbPass);

        return dbConnection;
    }

    // Write to the database and signup the User
    public void signUpUser(User user) {
        String insert = "INSERT INTO " + Const.USERS_TABLE + "("+Const.USERS_FIRSTNAME
                +","+Const.USERS_LASTNAME+","+Const.USERS_USERNAME+","
                +Const.USERS_PASSWORD+","+Const.USERS_LOCATION+","
                +Const.USERS_GENDER+")" + "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getLocation());
            preparedStatement.setString(6, user.getGender());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resultSet = null;

        // Validate that the form entry for username and password is not empty
        if (!user.getUserName().equals("") || !user.getPassword().equals("")) {
            // SQL Query to ask for requested username and password.
            String query = "SELECT * FROM " + Const.USERS_TABLE + " WHERE "
                    + Const.USERS_USERNAME + "=?" + " AND " + Const.USERS_PASSWORD + "=?";

            // select all from users where username="input" and password="password"
            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());

                resultSet = preparedStatement.executeQuery();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter your credentials");
        }

        return resultSet;
    }

    // Read (Get tasks)
    public ResultSet getTasks(User user) {
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.TASKS_TABLE + " WHERE "
                + Const.USERS_ID + "=?";

            // select all from tasks table where and password="password"
            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(user.getUid()));
                System.out.println(String.valueOf(user.getUid()));
                resultSet = preparedStatement.executeQuery();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        return resultSet;
    }

    // Create Tasks
    public void createTask(User user, Task task) {
        String insert = "INSERT INTO " + Const.TASKS_TABLE + "("+Const.USERS_ID + "," + Const.TASKS_DATE
                +","+Const.TASKS_DESCRIPTION + "," + Const.TASK_NAME + ")" + "VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            // Direct conversion from LocalDate to SQL date for insert.
            Date date = Date.valueOf(task.getDatecreated());

            preparedStatement.setInt(1, user.getUid());
            preparedStatement.setDate(2, date);
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getTask());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
