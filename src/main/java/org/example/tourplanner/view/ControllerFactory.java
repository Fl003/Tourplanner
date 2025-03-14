package org.example.tourplanner.view;

import org.example.tourplanner.viewmodel.*;

public class ControllerFactory {
    private final MainViewModel mainViewModel;
    private final SearchBarViewModel searchBarViewModel;
    private final TourOverviewModel tourOverviewModel;
    private final GeneralViewModel generalViewModel;
    private final MapViewModel mapViewModel;
    private final LogViewModel logViewModel;
    private final TourModalViewModel tourModalViewModel;

    public ControllerFactory() {
        searchBarViewModel = new SearchBarViewModel();
        tourOverviewModel = new TourOverviewModel();
        generalViewModel = new GeneralViewModel();
        mapViewModel = new MapViewModel();
        logViewModel = new LogViewModel();
        mainViewModel = new MainViewModel();
        tourModalViewModel = new TourModalViewModel();
    }

    //
    // Factory-Method Pattern
    //
    public Object create(Class<?> controllerClass) {
        if (controllerClass == MainController.class) {
            return new MainController(mainViewModel);
        } else if (controllerClass == SearchBarController.class) {
            return new SearchBarController(searchBarViewModel);
        } else if (controllerClass == TourOverviewController.class) {
            return new TourOverviewController(tourOverviewModel);
        } else if (controllerClass == GeneralController.class) {
            return new GeneralController(generalViewModel);
        } else if (controllerClass == MapController.class) {
            return new MapController(mapViewModel);
        } else if (controllerClass == LogController.class) {
            return new LogController(logViewModel);
        } else if (controllerClass == TourModalController.class) {
            return new TourModalController(tourModalViewModel, tourOverviewModel);
        }
        throw new IllegalArgumentException("Unknown controller class: " + controllerClass);
    }


    //
    // Singleton-Pattern with early-binding
    //
    private static final ControllerFactory instance = new ControllerFactory();

    public static ControllerFactory getInstance() {
        return instance;
    }

}
