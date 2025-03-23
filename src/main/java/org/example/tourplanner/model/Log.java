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

    public String getDifficulty() {
        return difficulty.get();
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public Integer getTotalDistance() {
        return totalDistance.get();
    }

    public IntegerProperty totalDistanceProperty() {
        return totalDistance;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty.set(difficulty);
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance.set(totalDistance);
    }
}
