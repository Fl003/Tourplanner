package org.example.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.tourplanner.viewmodel.GeneralViewModel;

public class GeneralController {
    private final GeneralViewModel generalViewModel;

    @FXML
    public TextField name;
    @FXML
    public TextField startingPoint;
    @FXML
    public Label distance;
    @FXML
    public TextField transportType;
    @FXML
    public TextField destination;
    @FXML
    public TextArea description;
    @FXML
    public Label estimatedTime;
    @FXML
    public HBox popularityStars;
    @FXML
    public HBox childFriendlinessStars;

    private final Image filledStar = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/star_yellow.png"));


    public GeneralController(GeneralViewModel generalViewModel) {
        this.generalViewModel = generalViewModel;
    }

    public void initialize() {
        name.textProperty().bindBidirectional(generalViewModel.nameProperty());
        startingPoint.textProperty().bindBidirectional(generalViewModel.startingPointProperty());
        destination.textProperty().bindBidirectional(generalViewModel.destinationProperty());
        transportType.textProperty().bindBidirectional(generalViewModel.transportTypeStringProperty());
        description.textProperty().bindBidirectional(generalViewModel.descriptionProperty());
        estimatedTime.textProperty().bindBidirectional(generalViewModel.estimatedTimeProperty());
        distance.textProperty().bindBidirectional(generalViewModel.distanceProperty());

        generalViewModel.popularityProperty().addListener((observable, oldValue, newValue) -> {
            popularityStars.getChildren().clear();
            for (int i = 0; i < newValue.intValue(); i++) {
                ImageView star = new ImageView(filledStar);
                star.setFitHeight(15);
                star.setFitWidth(15);
                popularityStars.getChildren().add(star);
            }
        });
        generalViewModel.childFriendlinessProperty().addListener((observable, oldValue, newValue) -> {
            childFriendlinessStars.getChildren().clear();
            for (int i = 0; i < newValue.intValue(); i++) {
                ImageView star = new ImageView(filledStar);
                star.setFitHeight(15);
                star.setFitWidth(15);
                childFriendlinessStars.getChildren().add(star);
            }
        });
    }
}
