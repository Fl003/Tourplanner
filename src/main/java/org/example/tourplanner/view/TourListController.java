package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.viewmodel.TourListViewModel;
import java.util.Locale;

public class TourListController {
    @FXML
    public ListView<Tour> tourList;

    private final TourListViewModel tourListViewModel;

    public TourListController(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
    }

    @FXML
    void initialize() {
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

                    {
                        hbox.setAlignment(Pos.CENTER);
                        HBox.setMargin(editBtn, new Insets(0, 5, 0, 20));
                        editBtn.setGraphic(new ImageView(imgEdit));
                        editBtn.setStyle("-fx-background-color: orange;");
                        editBtn.setOnAction(event -> {
                            Tour tour = getItem();
                            if (tour != null) {
                                System.out.println(tour.getName() + " edited");
                                editBtn(tour);
                            }

                        });
                        deleteBtn.setGraphic(new ImageView(imgDelete));
                        deleteBtn.setStyle("-fx-background-color: darkred;");
                        deleteBtn.setOnAction(event -> {
                            Tour tour = getItem();
                            if (tour != null) {
                                System.out.println(tour.getName() + " deleted");
                                deleteBtn();
                            }
                        });
                        HBox.setHgrow(spacer, Priority.ALWAYS);
                        hbox.getChildren().addAll(nameLabel, spacer, editBtn, deleteBtn);
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
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Parent root = FXMLDependencyInjection.load("TourModal.fxml", Locale.GERMAN);
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Create Tour");
            dialogStage.setMinHeight(251.0);
            dialogStage.setMinWidth(742.0);

            dialogStage.showAndWait();
            // TODO: how to call function from other controller????
            //MainController.showSuccess("message");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void showTourModal(Tour selectedTour){
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = FXMLDependencyInjection.getLoader("TourModal.fxml", Locale.GERMAN);
            Parent root = loader.load();

            TourModalController controller = loader.getController();
            controller.setTour(selectedTour);

            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Edit Tour");
            dialogStage.setMinHeight(251.0);
            dialogStage.setMinWidth(742.0);
            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBtn() {
        tourListViewModel.deleteTour(tourList.getSelectionModel().getSelectedItem());
    }
    public void editBtn(Tour selectedTour){
        //tourListViewModel.editTour(tourList.getSelectionModel().getSelectedItem());
        if(selectedTour == null){return;}
        showTourModal(selectedTour);
    }

}
