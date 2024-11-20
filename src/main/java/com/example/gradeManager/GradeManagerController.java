package com.example.gradeManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Contains functionalities of application.
 *
 * @author Auroba Ahmad
 */

public class GradeManagerController {
    @FXML
    private TextField categoryField;

    @FXML
    private ListView<String> listview;

    @FXML
    private TextField nameField;

    @FXML
    private TextField scoreField;

    /**
     * Adds grades to the database when the Add Grades to DB button is clicked.
     */
    @FXML
    protected void onAddGradeBtnClicked() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String score = scoreField.getText();

        //if the name, category, or score text fields are empty or if the text does not match the requirements then an alert window will show.
        if ((!name.matches("[a-zA-Z\\d\\s]+") || !category.matches("[a-zA-Z]+") || !score.matches("\\d+")) ||
                (name.isEmpty() || category.isEmpty() || score.isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Unable to add new grades to database. One or more of the input values are incorrect.");
            alert.showAndWait();

            //if the requirements are passed then the content of the fields will be added to the database.
        } else {
            int scoreInt = Integer.valueOf(score);
            String dbFilePath = ".//Persons.accdb";
            String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
            Connection conn;
            try {
                conn = DriverManager.getConnection(databaseURL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            insertDataToDB(conn, name, category, scoreInt);
            nameField.clear();
            categoryField.clear();
            scoreField.clear();
        }
    }

    /**
     * The grades in the database are displayed on the listview when the Display Grades from DB button is clicked.
     */
    @FXML
    protected void onDisplayGradesBtnClicked() {
        String dbFilePath = ".//Persons.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        Connection conn;
        //if connection to db fails it throws an exception
        try {
            conn = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        showDataFromDB(conn);
    }

    /**
     * The data from the Json file is loaded into the database when the menu item Load DB from JSON is clicked.
     */
    @FXML
    protected void onLoadDBFromJSONCalled() {
        String dbFilePath = ".//Persons.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        Connection conn;
        //if connection to db fails it throws an exception
        try {
            conn = DriverManager.getConnection(databaseURL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //calls the method to delete existing rows in the db
        deleteRowsFromDB(conn);
        //builds gson
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileReader fr;
        //if file is not found it throws an exception
        try {
            fr = new FileReader("grades.json");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Grade[] g = gson.fromJson(fr, Grade[].class);
        for (Grade grades : g) {
            String name = grades.getName();
            String category = grades.getCategory();
            int score = grades.getScore();
            insertDataToDB(conn, name, category, score);
        }
    }

    /**
     * The application closes when this menu item Exit is clicked.
     */
    @FXML
    protected void onMenuItemExitCalled() {
        Platform.exit();
    }

    /**
     * This is the first method called after the application loads, it calls the createDBandTable method.
     */
    public void initialize() {
        System.out.println("initialize called");
        createDBandTable();
    }

    /**
     * This method creates the database if it does not already exist and creates a table called Grades.
     */
    public void createDBandTable() {
        String dbFilePath = ".//Persons.accdb";
        String databaseURL = "jdbc:ucanaccess://" + dbFilePath;
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            try (Database db =
                         DatabaseBuilder.create(Database.FileFormat.V2010, new File(dbFilePath))) {
                System.out.println("The database file has been created.");
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }
            try {
                Connection conn = DriverManager.getConnection(databaseURL);
                String sql;
                sql = "CREATE TABLE Grades (Name nvarchar(255), Category nvarchar(255), Score int";
                Statement createTableStatement = conn.createStatement();
                createTableStatement.execute(sql);
                conn.commit();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }

    }

    /**
     * This method inserts the data into the table.
     *
     * @param conn     variable for connection
     * @param name     variable to hold the name
     * @param category variable to hold the category
     * @param score    variable to hold the score
     */
    public void insertDataToDB(Connection conn, String name, String category, int score) {
        String sql = "INSERT INTO Grades (name, category, score) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, category);
            preparedStatement.setInt(3, score);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This displays the data onto the listview from the database.
     *
     * @param conn variable for connection
     */

    public void showDataFromDB(Connection conn) {
        try {
            String tableName = "Grades";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select * from " + tableName);
            //goes through the rows of the table and retrieves the information
            while (result.next()) {
                String name = result.getString("Name");
                String category = result.getString("Category");
                int score = result.getInt("Score");
                //display the information on the listview
                ObservableList<String> grades = listview.getItems();
                grades.add(name + ", " + category + ", " + score);
                //   deleteRowsFromDB(conn);
            }
        } catch (SQLException except) {
            except.printStackTrace();
        }
    }

    /**
     * This method deletes any existing rows in the table.
     *
     * @param conn variable for connection
     */
    public void deleteRowsFromDB(Connection conn) {
        String sql = "DELETE FROM Grades";
        PreparedStatement preparedStatement = null;
        //if connection to db fails it throws an exception
        try {
            preparedStatement = conn.prepareStatement(sql);
            int rowsDeleted = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

  