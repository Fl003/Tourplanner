package org.example.tourplanner.tests;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import org.example.tourplanner.viewmodel.LogModalViewModel;
import org.example.tourplanner.viewmodel.LogViewModel;
import org.example.tourplanner.viewmodel.TourListViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//1
public class LogControllerTest extends ApplicationTest {

    private LogViewModel logViewModel;
    private LogModalViewModel logModalViewModel;
    private TourListViewModel tourListViewModel;
    private org.example.tourplanner.view.LogController controller;

    @BeforeEach
    void setup() {
        logViewModel = mock(LogViewModel.class);
        logModalViewModel = mock(LogModalViewModel.class);
        tourListViewModel = mock(TourListViewModel.class);

        // Setup dummy logs list for logViewModel.getLogs()
        when(logViewModel.getLogs()).thenReturn(FXCollections.observableArrayList());

        controller = new org.example.tourplanner.view.LogController(logViewModel, logModalViewModel, tourListViewModel);

        // Manually initialize TableView and columns because not loading fxml here
        controller.logTable = new TableView<>();
        controller.columnDateTime = mock(javafx.scene.control.TableColumn.class);
        controller.columnDifficulty = mock(javafx.scene.control.TableColumn.class);
        controller.columnTotalDistance = mock(javafx.scene.control.TableColumn.class);
        controller.columnTotalTime = mock(javafx.scene.control.TableColumn.class);
        controller.columnRating = mock(javafx.scene.control.TableColumn.class);
        controller.columnComment = mock(javafx.scene.control.TableColumn.class);
    }
//ensures that when the user creates a new log, the controller sets the currently selected tour in the modal view model
    @Test
    void createNewLog_ShouldClearLogAndShowModal() {
        doNothing().when(logModalViewModel).clearLog();
        doNothing().when(logModalViewModel).setSelectedTour(any());
        assertDoesNotThrow(() -> controller.createNewLog());
        verify(logModalViewModel).clearLog();
        verify(logModalViewModel).setSelectedTour(any());
    }
}
