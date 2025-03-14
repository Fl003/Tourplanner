package org.example.tourplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TourplannerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TourplannerApplication.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 995, 600);
        stage.setTitle("Tourplanner");
        stage.setMinHeight(650);
        stage.setMinWidth(1050);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch();
    }
}