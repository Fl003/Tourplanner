package org.example.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.viewmodel.TourOverviewModel;

public class TourOverviewController {
    @FXML
    public ListView<Tour> tourList;

    private final TourOverviewModel tourOverviewModel;

    public TourOverviewController(TourOverviewModel tourOverviewModel) {
        this.tourOverviewModel = tourOverviewModel;
    }

    @FXML
    void initialize() {
        tourList.setItems(tourOverviewModel.getObservableTours());
    }
}
