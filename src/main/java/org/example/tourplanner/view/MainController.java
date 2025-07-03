package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.service.PdfService;
import org.example.tourplanner.viewmodel.TourModalViewModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private static final Logger logger = LogManager.getLogger(MainController.class);
    private final TourModalViewModel tourModalViewModel;
    private final PdfService pdfService;

    public MainController(TourModalViewModel tourModalViewModel, PdfService pdfService) {
        this.tourModalViewModel = tourModalViewModel;
        this.pdfService = pdfService;
    }

    public void showTourModal(ActionEvent actionEvent) throws IOException {
        tourModalViewModel.clearTour();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLDependencyInjection.load("TourModal.fxml", Locale.ENGLISH);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Create Tour");
        dialogStage.setMinHeight(251.0);
        dialogStage.setMinWidth(742.0);
        dialogStage.showAndWait();
    }

    public void downloadPdf(ActionEvent actionEvent) {
        logger.info("download Pdf");

        byte[] pdfBytes = this.pdfService.getSummarizeReportPdf();
        logger.info("got pdf");
        if (pdfBytes != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("PDF speichern");
            fileChooser.setInitialFileName("summarize-report.pdf");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Dateien", "*.pdf")
            );

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    Files.write(file.toPath(), pdfBytes);
                } catch (IOException e) {
                    logger.error("Error with saving pdf {}", e.getMessage());
                }
            }
        }
    }
}