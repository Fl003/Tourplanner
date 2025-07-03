package org.example.tourplanner.tests;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.viewmodel.TourListViewModel;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import java.time.LocalDate;
import java.util.Locale;

//notes about tests:
// had to be put into program arguments of test in order to be able to run the tests
// --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED

//4
public class UITests extends ApplicationTest {

    private TourListViewModel mockTourListViewModel;

    @Override
    public void start (Stage stage) throws Exception {
        ListView<Object> tourList = new ListView<>();
        Parent root = FXMLDependencyInjection.load("MainView.fxml", Locale.ENGLISH);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
//Simulates UI interaction: opening the log modal, filling in data, selecting a rating, and saving the log
    @Test
    public void openAndFillLogModal() throws InterruptedException {
        clickOn("#newLog");

        waitForFxEvents();

        clickOn("#logDate");
        interact(() -> {
            DatePicker datePicker = lookup("#logDate").query();
            datePicker.setValue(LocalDate.of(2025, 3, 24));
        });

        clickOn("#logHour").write("15");
        clickOn("#logMinute").write("12");
        clickOn("#difficulty").write("easy");
        clickOn("#totalDistance").write("1000");
        clickOn("#totalTime").write("1000");
        clickOn("#comment").write("viele schöne Aussichtspunkte!");
        clickOn("#star4");
        clickOn("#saveLog");
    }

//not being able to write a number greater than 24 in the hours field
    @Test
    public void NumbersGreaterThan24(){

        clickOn("#newLog");

        waitForFxEvents();

        clickOn("#logDate");
        interact(() -> {
            DatePicker datePicker = lookup("#logDate").query();
            datePicker.setValue(LocalDate.of(2025, 3, 23));
        });

        clickOn("#logHour").write("25");
        clickOn("#logMinute").write("25");
        clickOn("#difficulty").write("medium");
        clickOn("#totalDistance").write("1000");
        clickOn("#totalTime").write("100");
        clickOn("#comment").write("good tour");
        clickOn("#star3");
        clickOn("#saveLog");
    }

//adding a log to a specific tour
    @Test
    public void addLog() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        //mockTourListViewModel = new TourListViewModel();
        //Tour tour = new Tour("ZweiterTest","Vienna", "Germany", TransportType.BUS, "schön");
        //mockTourListViewModel.addTour(tour);

        ListView<Tour> tourListView = lookup("#tourList").query();
        clickOn(tourListView.lookup(".list-cell"));

        clickOn("#newLog");

        waitForFxEvents();

        clickOn("#logDate");
        interact(() -> {
            DatePicker datePicker = lookup("#logDate").query();
            datePicker.setValue(LocalDate.of(2025, 3, 24));
        });

        clickOn("#logHour").write("17");
        clickOn("#logMinute").write("25");
        clickOn("#difficulty").write("easy");
        clickOn("#totalDistance").write("10000");
        clickOn("#totalTime").write("100");
        clickOn("#comment").write("wunderschöne Wanderwege!");
        clickOn("#star5");
        clickOn("#saveLog");

        assertTrue(tourListView.getSelectionModel().getSelectedItems().size() > 0);
    }
//clicking on a tour to ensure viewing data works
    @Test
    public void showData() throws NoSuchFieldException {
        //mockTourListViewModel = new TourListViewModel();
        //Tour tour = new Tour("ZweiterTest", "Vienna", "Germany", TransportType.BUS, "schön");
        //mockTourListViewModel.addTour(tour);

        ListView<Tour> tourListView = lookup("#tourList").query();
        clickOn(tourListView.lookup(".list-cell"));

        //assertEquals("ZweiterTest", tour.getName());
    }

}
