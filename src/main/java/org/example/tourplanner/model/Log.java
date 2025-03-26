package org.example.tourplanner.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Log {
    private final ObjectProperty<LocalDate> date;
    private final IntegerProperty hour;
    private final IntegerProperty minute;
    private final StringProperty comment;
    private final StringProperty difficulty;
    private final IntegerProperty totalDistance;
    private final IntegerProperty totalTime;
    private final IntegerProperty rating;

    public Log(LocalDate date, Integer hour, Integer minute, String comment, String difficulty, int totalDistance, int totalTime, int rating) {
        this.date = new SimpleObjectProperty<>(date);
        this.hour = new SimpleIntegerProperty(hour);
        this.minute = new SimpleIntegerProperty(minute);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleStringProperty(difficulty);
        this.totalDistance = new SimpleIntegerProperty(totalDistance);
        this.totalTime = new SimpleIntegerProperty(totalTime);
        this.rating = new SimpleIntegerProperty(rating);
    }

    public Log() {
        this.date = new SimpleObjectProperty<>(LocalDate.now());
        this.hour = new SimpleIntegerProperty(0);
        this.minute = new SimpleIntegerProperty(0);
        this.comment = new SimpleStringProperty("");
        this.difficulty = new SimpleStringProperty("");
        this.totalDistance = new SimpleIntegerProperty(0);
        this.totalTime = new SimpleIntegerProperty(0);
        this.rating = new SimpleIntegerProperty(0);
    }

    public StringProperty difficultyProperty() { return difficulty; }
    public IntegerProperty totalDistanceProperty() {
        return totalDistance;
    }
    public IntegerProperty totalTimeProperty() { return totalTime; }
    public IntegerProperty ratingProperty() { return rating; }
    public StringProperty commentProperty() { return comment; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public IntegerProperty hourProperty() { return hour; }
    public IntegerProperty minuteProperty() { return minute; }

    public void setDifficulty(String difficulty) {
        this.difficulty.set(difficulty);
    }
    public void setTotalDistance(int totalDistance) {
        this.totalDistance.set(totalDistance);
    }
    public void setTotalTime(int totalTime) { this.totalTime.set(totalTime); }
    public void setRating(int rating) { this.rating.set(rating); }
    public void setComment(String comment) { this.comment.set(comment); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public void setHour(int hour) { this.hour.set(hour); }
    public void setMinute(int minute) { this.minute.set(minute); }
}
