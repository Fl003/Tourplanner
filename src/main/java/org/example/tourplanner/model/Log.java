package org.example.tourplanner.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Log {
    private final Long id;
    private final LongProperty tourId;
    private final ObjectProperty<LocalDate> date;
    private final IntegerProperty hour;
    private final IntegerProperty minute;
    private final StringProperty comment;
    private final ObjectProperty<Difficulty> difficulty;
    private final DoubleProperty totalDistance;
    private final IntegerProperty totalTime;
    private final IntegerProperty rating;

    public Log(Long id, Long tourId, LocalDate date, Integer hour, Integer minute, String comment, String difficulty, double totalDistance, int totalTime, int rating) {
        this.id = id;
        this.tourId = new SimpleLongProperty(tourId);
        this.date = new SimpleObjectProperty<>(date);
        this.hour = new SimpleIntegerProperty(hour);
        this.minute = new SimpleIntegerProperty(minute);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleObjectProperty<>(Difficulty.valueOf(difficulty));
        this.totalDistance = new SimpleDoubleProperty(totalDistance);
        this.totalTime = new SimpleIntegerProperty(totalTime);
        this.rating = new SimpleIntegerProperty(rating);
    }

    public Log(Long tourId, LocalDate date, Integer hour, Integer minute, String comment, String difficulty, int totalDistance, int totalTime, int rating) {
        this.id = null;
        this.tourId = new SimpleLongProperty(tourId);
        this.date = new SimpleObjectProperty<>(date);
        this.hour = new SimpleIntegerProperty(hour);
        this.minute = new SimpleIntegerProperty(minute);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleObjectProperty<>(Difficulty.valueOf(difficulty));
        this.totalDistance = new SimpleDoubleProperty(totalDistance);
        this.totalTime = new SimpleIntegerProperty(totalTime);
        this.rating = new SimpleIntegerProperty(rating);
    }

    public Log() {
        this.id = null;
        this.tourId = new SimpleLongProperty(0L);
        this.date = new SimpleObjectProperty<>(LocalDate.now());
        this.hour = new SimpleIntegerProperty(0);
        this.minute = new SimpleIntegerProperty(0);
        this.comment = new SimpleStringProperty("");
        this.difficulty = new SimpleObjectProperty<>(Difficulty.Easy);
        this.totalDistance = new SimpleDoubleProperty(0.0);
        this.totalTime = new SimpleIntegerProperty(0);
        this.rating = new SimpleIntegerProperty(0);
    }

    public Long getId() { return id; }
    public Long getTourId() { return tourId.get(); }
    public Difficulty getDifficulty() { return difficulty.get(); }
    public Double getTotalDistance() { return totalDistance.get(); }
    public Integer getTotalTime() { return totalTime.get(); }
    public Integer getRating() { return rating.get(); }
    public String getComment() { return comment.get(); }
    public LocalDate getDate() { return date.get(); }
    public Integer getHour() { return hour.get(); }
    public Integer getMinute() { return minute.get(); }

    public ObjectProperty<Difficulty> difficultyProperty() { return difficulty; }
    public DoubleProperty totalDistanceProperty() { return totalDistance; }
    public IntegerProperty totalTimeProperty() { return totalTime; }
    public IntegerProperty ratingProperty() { return rating; }
    public StringProperty commentProperty() { return comment; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public IntegerProperty hourProperty() { return hour; }
    public IntegerProperty minuteProperty() { return minute; }

    public void setTourId(Long tourId) { this.tourId.set(tourId); }
    public void setDifficulty(String difficulty) {
        this.difficulty.set(Difficulty.valueOf(difficulty));
    }
    public void setTotalDistance(double totalDistance) {
        this.totalDistance.set(totalDistance);
    }
    public void setTotalTime(int totalTime) { this.totalTime.set(totalTime); }
    public void setRating(int rating) { this.rating.set(rating); }
    public void setComment(String comment) { this.comment.set(comment); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public void setHour(int hour) { this.hour.set(hour); }
    public void setMinute(int minute) { this.minute.set(minute); }
}
