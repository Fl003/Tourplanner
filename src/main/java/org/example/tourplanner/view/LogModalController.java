package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.tourplanner.viewmodel.LogModalViewModel;

import java.util.ArrayList;
import java.util.List;

public class LogModalController {
    private final LogModalViewModel logModalViewModel;
    @FXML
    public Label modalTitle;
    @FXML
    public TextField difficulty;
    @FXML
    public TextField totalDistance;
    @FXML
    public TextField totalTime;
    @FXML
    public TextArea comment;
    @FXML
    public ImageView star2;
    @FXML
    public ImageView star5;
    @FXML
    public ImageView star1;
    @FXML
    public ImageView star3;
    @FXML
    public ImageView star4;
    @FXML
    public HBox statusBar;
    @FXML
    public Label statusMessage;
    @FXML
    public DatePicker logDate;
    @FXML
    public TextField logHour;
    @FXML
    public TextField logMinute;

    private List<ImageView> stars = new ArrayList<>();

    public LogModalController(LogModalViewModel logModalViewModel) {
        this.logModalViewModel = logModalViewModel;
    }

    public void initialize() {
        stars = List.of(star1, star2, star3, star4, star5);
        stars.forEach(button -> button.setOnMouseClicked(mouseEvent -> {
            changeRating(button);
        }));

        logHour.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
        logMinute.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });
        addNumericConstraint(logHour, 0, 24);
        addNumericConstraint(logMinute, 0, 60);

        logDate.valueProperty().bindBidirectional(logModalViewModel.dateProperty());
        logHour.textProperty().bindBidirectional(logModalViewModel.hourProperty());
        logMinute.textProperty().bindBidirectional(logModalViewModel.minuteProperty());
        difficulty.textProperty().bindBidirectional(logModalViewModel.difficultyProperty());
        totalDistance.textProperty().bindBidirectional(logModalViewModel.totalDistanceProperty());
        totalTime.textProperty().bindBidirectional(logModalViewModel.totalTimeProperty());
        comment.textProperty().bindBidirectional(logModalViewModel.commentProperty());
        changeRating(stars.get(logModalViewModel.ratingProperty().get()));
    }

    public void changeRating(ImageView button) {
        Image outlineStar = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/star.png"));
        Image filledStar = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/star_yellow.png"));

        String id = button.getId();
        int selectedRating = Integer.parseInt(id.substring(4, 5));
        logModalViewModel.setRating(selectedRating);

        boolean filled = true;
        for (ImageView star : stars) {
            if (filled)
                star.setImage(filledStar);
            else
                star.setImage(outlineStar);
            if (id.equals(star.getId()))
                filled = false;
        }
    }

    private void addNumericConstraint(TextField textField, int min, int max) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) return;
            try {
                int value = Integer.parseInt(newValue);
                if (value < min || value > max) {
                    textField.setText(oldValue); // Revert invalid input
                }
            } catch (NumberFormatException e) {
                textField.setText(oldValue); // Revert non-numeric input
            }
        });
    }

    public void saveLog(ActionEvent actionEvent) {
        // create new or save modified log
    }
}
