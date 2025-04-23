package org.example.tourplanner.view;

import org.example.tourplanner.service.TourService;
import org.example.tourplanner.viewmodel.*;

public class ControllerFactory {
    private final MainViewModel mainViewModel;
    private final SearchBarViewModel searchBarViewModel;
    private final TourListViewModel tourListViewModel;
    private final GeneralViewModel generalViewModel;
    private final MapViewModel mapViewModel;
    private final LogViewModel logViewModel;
    private final TourModalViewModel tourModalViewModel;
    private final LogModalViewModel logModalViewModel;

    // Services
    private final TourService tourService;

    public ControllerFactory() {
        tourService = new TourService();

        searchBarViewModel = new SearchBarViewModel();
        tourListViewModel = new TourListViewModel(tourService);
        generalViewModel = new GeneralViewModel(tourListViewModel);
        mapViewModel = new MapViewModel();
        logViewModel = new LogViewModel(tourListViewModel);
        mainViewModel = new MainViewModel();
        tourModalViewModel = new TourModalViewModel();
        logModalViewModel = new LogModalViewModel();
    }

    //
    // Factory-Method Pattern
    //
    public Object create(Class<?> controllerClass) {
        if (controllerClass == MainController.class) {
            return new MainController(mainViewModel);
        } else if (controllerClass == SearchBarController.class) {
            return new SearchBarController(searchBarViewModel);
        } else if (controllerClass == TourListController.class) {
            return new TourListController(tourListViewModel, tourService);
        } else if (controllerClass == GeneralController.class) {
            return new GeneralController(generalViewModel);
        } else if (controllerClass == MapController.class) {
            return new MapController(mapViewModel);
        } else if (controllerClass == LogController.class) {
            return new LogController(logViewModel, tourListViewModel, logModalViewModel);
        } else if (controllerClass == TourModalController.class) {
            return new TourModalController(tourModalViewModel, tourListViewModel, tourService);
        } else if (controllerClass == LogModalController.class) {
            return new LogModalController(logModalViewModel, tourListViewModel);
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
