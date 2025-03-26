package org.example.tourplanner.viewmodel;

import javafx.beans.property.*;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;

import java.time.LocalDate;

public class LogModalViewModel {
    private Tour selectedTour;
    private Log currentLog;

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty hour = new SimpleStringProperty();
    private final StringProperty minute = new SimpleStringProperty();
    private final StringProperty comment = new SimpleStringProperty();
    private final StringProperty difficulty = new SimpleStringProperty();
    private final StringProperty totalDistance = new SimpleStringProperty();
    private final StringProperty totalTime = new SimpleStringProperty();
    private final IntegerProperty rating  = new SimpleIntegerProperty();

    public LogModalViewModel() {
        this.currentLog = null;
        this.selectedTour = null;
    }

    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty hourProperty() { return hour; }
    public StringProperty minuteProperty() { return minute; }
    public StringProperty commentProperty() { return comment; }
    public StringProperty difficultyProperty() { return difficulty; }
    public StringProperty totalDistanceProperty() { return totalDistance; }
    public StringProperty totalTimeProperty() { return totalTime; }
    public IntegerProperty ratingProperty() { return rating; }

    public void setDate(LocalDate date) { this.date.set(date); }
    public void setHour(int hour) { this.hour.set(String.valueOf(hour)); }
    public void setMinute(int minute) { this.minute.set(String.valueOf(minute)); }
    public void setComment(String comment) { this.comment.set(comment); }
    public void setDifficulty(String difficulty) { this.difficulty.set(difficulty); }
    public void setTotalDistance(String totalDistance) { this.totalDistance.set(totalDistance); }
    public void setTotalTime(String totalTime) { this.totalTime.set(totalTime); }
    public void setRating(int rating) { this.rating.set(rating); }

    public void setLog(Log log) {
        this.currentLog = log;
        setDate(log.dateProperty().get());
        setHour(log.hourProperty().get());
        setMinute(log.minuteProperty().get());
        setComment(log.commentProperty().get());
        setDifficulty(log.difficultyProperty().get());
        setTotalDistance(String.valueOf(log.totalDistanceProperty().get()));
        setTotalTime(String.valueOf(log.totalTimeProperty().get()));
        setRating(log.ratingProperty().get());
    }

    public void clearLog() {
        this.currentLog = null;
        date.set(null);
        hour.set(null);
        minute.set(null);
        comment.set(null);
        difficulty.set(null);
        totalDistance.set(null);
        totalTime.set(null);
        rating.set(0);
    }

    public void setSelectedTour(Tour tour) {
        this.selectedTour = tour;
    }

    public Tour getSelectedTour() {
        return selectedTour;
    }

    public boolean isCreate() {
        return currentLog == null;
    }

    public Log getCurrentLog() { return currentLog; }
}
