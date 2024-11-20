package com.example.gradeManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class loads the application and contains the main method.
 *
 * @author Auroba Ahmad
 */

public class HelloApplication extends Application {

    /**
     * Loads the stage and creates the scene.
     * @param stage name for Stage.
     * @throws IOException throws exception if stage cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Grade Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the application.
     * @param args name for String[].
     */
    public static void main(String[] args) {launch();}
}