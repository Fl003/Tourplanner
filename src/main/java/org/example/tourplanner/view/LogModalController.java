package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.tourplanner.viewmodel.LogModalViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LogModalController {
    private final LogModalViewModel logModalViewModel;

    @FXML
    public Label modalTitle;
    @FXML
    public ComboBox<String> difficulty;
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
    public Button saveLog;
    @FXML
    ResourceBundle resources;

    private List<ImageView> stars = new ArrayList<>();

    public LogModalController(LogModalViewModel logModalViewModel) {
        this.logModalViewModel = logModalViewModel;
    }

    @FXML
    public void initialize() {
        stars = List.of(star1, star2, star3, star4, star5);
        stars.forEach(button -> button.setOnMouseClicked(mouseEvent -> changeRating(button)));

        LocalDate today = LocalDate.now();

        // Set the cell factory to disable dates after today
        logDate.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);

                        if (date.isAfter(today)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffcccc;"); // Optional: Style for disabled dates
                        }
                    }
                };
            }
        });

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
        totalDistance.textProperty().bindBidirectional(logModalViewModel.totalDistanceProperty());
        totalTime.textProperty().bindBidirectional(logModalViewModel.totalTimeProperty());
        comment.textProperty().bindBidirectional(logModalViewModel.commentProperty());

        // change Heading and change Rating
        if (logModalViewModel.getId() == null) {
            modalTitle.setText(this.resources.getString("Log_Create"));
        } else {
            modalTitle.setText(this.resources.getString("Log_Modify"));
            changeRating(stars.get(logModalViewModel.ratingProperty().get() - 1));
        }

        // set difficulty combobox items
        difficulty.setItems(this.logModalViewModel.getDifficulties());

        // if editing -> set difficulty otherwise select first one
        if (logModalViewModel.difficultyProperty().get() != null)
            difficulty.setValue(logModalViewModel.difficultyProperty().get());
        else
            difficulty.getSelectionModel().selectFirst();

        difficulty.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logModalViewModel.difficultyProperty().set(newValue);
        });
    }

    public void changeRating(ImageView button) {
        // template stars
        Image outlineStar = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/star.png"));
        Image filledStar = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/star_yellow.png"));

        // button id = star1, star2, star3, ...
        String id = button.getId();
        // delete star, return number
        int selectedRating = Integer.parseInt(id.substring(4, 5));
        logModalViewModel.setRating(selectedRating);

        boolean filled = true;
        // set filled star until id equals the starId, afterwards only outlined stars
        for (ImageView star : stars) {
            if (filled)
                star.setImage(filledStar);
            else
                star.setImage(outlineStar);
            if (id.equals(star.getId()))
                filled = false;
        }
    }

    // function takes min and max and ensures that input is between these two
    private void addNumericConstraint(TextField textField, int min, int max) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;
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
        if (!validInput()) return;

        logModalViewModel.saveLog();

        // close the modal
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean validInput() {
        // Check if the required fields are empty
        if (logModalViewModel.dateProperty().get() == null || logModalViewModel.hourProperty().isEmpty().get() || logModalViewModel.minuteProperty().isEmpty().get()|| logModalViewModel.commentProperty().isEmpty().get() || logModalViewModel.totalDistanceProperty().isEmpty().get() || logModalViewModel.totalTimeProperty().isEmpty().get()) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("BlankField"));
            return false;
        }

        // Validate the hour and minute fields to ensure they're numeric
        if (!logModalViewModel.hourProperty().get().matches("\\d{1,2}") || !logModalViewModel.minuteProperty().get().matches("\\d{1,2}")) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("InvalidTimeFormatLog"));
            return false;
        }

        // Validate the totalDistance and totalTime to ensure they're numeric
        if (!logModalViewModel.totalDistanceProperty().get().matches("\\d+(?:[.,]\\d{1,2})?(?:km|m)?") || !logModalViewModel.totalTimeProperty().get().matches("\\d+|\\d{1,2}:\\d{2}|\\d{1,2}:\\d{2}:\\d{2}|\\d+h(?: \\d+min)?(?: \\d+sec)?|\\d+min(?: \\d+sec)?|\\d+sec|\\d+h|\\d+min")) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("InvalidNumericValuesLog"));
            return false;
        }

        // Validate the rating field to ensure it's within a valid range (1-5)
        if (logModalViewModel.ratingProperty().get() < 1 || logModalViewModel.ratingProperty().get() > 5) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("InvalidRating"));
            return false;
        }
        return true;
    }
}
