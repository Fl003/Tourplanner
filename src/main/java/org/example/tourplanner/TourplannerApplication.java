package org.example.tourplanner;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class TourplannerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLDependencyInjection.load("MainView.fxml", Locale.ENGLISH);

        Scene scene = new Scene(root, 995, 600);
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