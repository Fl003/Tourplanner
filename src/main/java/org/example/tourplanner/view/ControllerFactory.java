package org.example.tourplanner.view;

import org.example.tourplanner.service.*;
import org.example.tourplanner.viewmodel.*;

public class ControllerFactory {
    private final SearchBarViewModel searchBarViewModel;
    private final TourListViewModel tourListViewModel;
    private final GeneralViewModel generalViewModel;
    private final MapViewModel mapViewModel;
    private final LogViewModel logViewModel;
    private final TourModalViewModel tourModalViewModel;
    private final LogModalViewModel logModalViewModel;

    // Services
    private final TourService tourService;
    private final LogService logService;
    private final ImportExportService importExportService;
    private final GeocodeService geocodeService;
    private final DirectionsService directionsService;
    private final PdfService pdfService;

    public ControllerFactory() {
        logService = new LogService();
        tourService = new TourService(logService);
        geocodeService = new GeocodeService();
        directionsService = new DirectionsService();
        pdfService = new PdfService();
        importExportService = new ImportExportService();

        tourListViewModel = new TourListViewModel(tourService);
        generalViewModel = new GeneralViewModel(tourListViewModel, tourService);
        mapViewModel = new MapViewModel(tourListViewModel, tourService);
        logViewModel = new LogViewModel(tourListViewModel, logService, tourService);
        tourModalViewModel = new TourModalViewModel(tourService, directionsService);
        logModalViewModel = new LogModalViewModel(logService);
        searchBarViewModel = new SearchBarViewModel(tourListViewModel, tourService);
    }

    //
    // Factory-Method Pattern
    //
    public Object create(Class<?> controllerClass) {
        if (controllerClass == MainController.class) {
            return new MainController(tourModalViewModel, tourListViewModel, pdfService, importExportService);
        } else if (controllerClass == SearchBarController.class) {
            return new SearchBarController(searchBarViewModel);
        } else if (controllerClass == TourListController.class) {
            return new TourListController(tourListViewModel, tourModalViewModel, pdfService);
        } else if (controllerClass == GeneralController.class) {
            return new GeneralController(generalViewModel);
        } else if (controllerClass == MapController.class) {
            return new MapController(mapViewModel, directionsService);
        } else if (controllerClass == LogController.class) {
            return new LogController(logViewModel, logModalViewModel, tourListViewModel);
        } else if (controllerClass == TourModalController.class) {
            return new TourModalController(tourModalViewModel);
        } else if (controllerClass == LogModalController.class) {
            return new LogModalController(logModalViewModel);
        } else if (controllerClass == AddressSelectionController.class) {
            return new AddressSelectionController(geocodeService);
        } else if (controllerClass == ExportDataController.class) {
            return new ExportDataController(tourService, importExportService);
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
