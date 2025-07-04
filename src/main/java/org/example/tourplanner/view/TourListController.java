package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.PdfService;
import org.example.tourplanner.service.TourService;
import org.example.tourplanner.viewmodel.TourListViewModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.example.tourplanner.viewmodel.TourModalViewModel;

import java.util.Locale;

public class TourListController {
    private static final Logger logger = LogManager.getLogger(TourListController.class);
    @FXML
    public ListView<Tour> tourList;
    @FXML
    public Button newTour;

    private final TourModalViewModel tourModalViewModel;
    private final TourListViewModel tourListViewModel;
    private final PdfService pdfService;

    public TourListController(TourListViewModel tourListViewModel, TourModalViewModel tourModalViewModel, PdfService pdfService) {
        this.tourListViewModel = tourListViewModel;
        this.tourModalViewModel = tourModalViewModel;
        this.pdfService = pdfService;
    }

    @FXML
    void initialize() {
        // set tours in ListView
        tourList.setItems(tourListViewModel.getObservableTours());
        tourList.getSelectionModel().selectedItemProperty().addListener(tourListViewModel.getChangeListener());

        tourList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Tour> call(ListView<Tour> param) {
                return new ListCell<>() {
                    private final HBox hbox = new HBox();
                    private final Label nameLabel = new Label();
                    private final Image imgEdit = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/edit.png"), 15, 15, true, true);
                    private final Button editBtn = new Button();
                    private final Image imgDelete = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/delete.png"), 15, 15, true, true);
                    private final Button deleteBtn = new Button();
                    private final Region spacer = new Region();
                    private final Button downloadBtn = new Button();
                    private final Image imgDownload = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/download.png"), 15, 15, true, true);

                    {
                        downloadBtn.setOnAction(event -> {
                           Tour tour = getItem();
                           if (tour != null)
                               downloadPdf(tour.getId());
                        });

                        editBtn.setOnAction(event -> {
                            Tour tour = getItem();
                            if (tour != null)
                                editBtn(tour);
                        });

                        deleteBtn.setOnAction(event -> {
                            Tour tour = getItem();
                            if (tour != null)
                                deleteBtn(tour.getId());
                        });

                        // styling
                        hbox.setAlignment(Pos.CENTER);
                        HBox.setMargin(editBtn, new Insets(0, 5, 0, 0));
                        HBox.setMargin(downloadBtn, new Insets(0, 5, 0, 20));
                        HBox.setHgrow(spacer, Priority.ALWAYS);
                        editBtn.setGraphic(new ImageView(imgEdit));
                        editBtn.setStyle("-fx-background-color: orange;");
                        deleteBtn.setGraphic(new ImageView(imgDelete));
                        deleteBtn.setStyle("-fx-background-color: darkred;");
                        downloadBtn.setGraphic(new ImageView(imgDownload));
                        downloadBtn.setStyle("-fx-background-color: purple;");
                        hbox.getChildren().addAll(nameLabel, spacer, downloadBtn , editBtn, deleteBtn);
                    }

                    @Override
                    protected void updateItem(Tour item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            nameLabel.setText(item.getName());
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
    }

    public void showTourModal(ActionEvent actionEvent) {
        try {
            tourModalViewModel.clearTour();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Parent root = FXMLDependencyInjection.load("TourModal.fxml", Locale.GERMAN);
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Create Tour");
            dialogStage.setMinHeight(251.0);
            dialogStage.setMinWidth(742.0);
            dialogStage.showAndWait();
            // get last created tour
            Tour tour = tourListViewModel.getLastCreatedTours();
            if (tour == null)
                tourListViewModel.loadTours();
            else
                tourListViewModel.reloadTour(tour.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTourModal(Tour selectedTour){
        try {
            tourModalViewModel.setTour(selectedTour);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Parent root = FXMLDependencyInjection.load("TourModal.fxml", Locale.GERMAN);
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Edit Tour");
            dialogStage.setMinHeight(251.0);
            dialogStage.setMinWidth(742.0);
            dialogStage.showAndWait();
            tourListViewModel.reloadTour(selectedTour.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBtn(Long tourId) {
        this.tourListViewModel.deleteTour(tourId);
    }

    public void editBtn(Tour selectedTour){
        if(selectedTour == null) { return; }
        showTourModal(selectedTour);
    }

    public void downloadPdf(Long tourId) {
        byte[] pdfBytes = this.pdfService.getTourReportPdf(tourId);
        if (pdfBytes != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("PDF speichern");
            fileChooser.setInitialFileName("tour-report-" + tourId + ".pdf");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Dateien", "*.pdf")
            );

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    Files.write(file.toPath(), pdfBytes);
                } catch (IOException e) {
                    logger.error("Saving of PDF not successful "+ e.getMessage());
                }
            }
        }
    }
}
