package org.example.tourplanner.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

import java.time.LocalDate;

public class LogModalViewModel {


    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty hour = new SimpleStringProperty();
    private final StringProperty minute = new SimpleStringProperty();
    private final StringProperty comment = new SimpleStringProperty();
    private final StringProperty difficulty = new SimpleStringProperty();
    private final StringProperty totalDistance = new SimpleStringProperty();
    private final StringProperty totalTime = new SimpleStringProperty();
    private final IntegerProperty rating  = new SimpleIntegerProperty();

    public LogModalViewModel() {

    }

    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty hourProperty() { return hour; }
    public StringProperty minuteProperty() { return minute; }
    public StringProperty commentProperty() { return comment; }
    public StringProperty difficultyProperty() { return difficulty; }
    public StringProperty totalDistanceProperty() { return totalDistance; }
    public StringProperty totalTimeProperty() { return totalTime; }
    public IntegerProperty ratingProperty() { return rating; }

    public void setRating(int rating) { this.rating.set(rating); }
}
