package org.example.tourplanner.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.LogDto;
import org.example.tourplanner.model.Difficulty;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.LogService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LogModalViewModel {
    private final LogService logService;

    private final ObservableList<String> difficulties = FXCollections.observableArrayList();

    private Long id = null;
    private Long tourId = null;
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty hour = new SimpleStringProperty();
    private final StringProperty minute = new SimpleStringProperty();
    private final StringProperty comment = new SimpleStringProperty();
    private final StringProperty difficulty = new SimpleStringProperty();
    private final StringProperty totalDistance = new SimpleStringProperty();
    private final StringProperty totalTime = new SimpleStringProperty();
    private final IntegerProperty rating  = new SimpleIntegerProperty();

    public LogModalViewModel(LogService logService) {
        this.logService = logService;

        difficulties.add(Difficulty.Easy.toString());
        difficulties.add(Difficulty.Medium.toString());
        difficulties.add(Difficulty.Difficult.toString());
    }

    public ObservableList<String> getDifficulties() {
        return difficulties;
    }

    public Long getId() { return id; }
    public Long getTourId() { return tourId; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty hourProperty() { return hour; }
    public StringProperty minuteProperty() { return minute; }
    public StringProperty commentProperty() { return comment; }
    public StringProperty difficultyProperty() { return difficulty; }
    public StringProperty totalDistanceProperty() { return totalDistance; }
    public StringProperty totalTimeProperty() { return totalTime; }
    public IntegerProperty ratingProperty() { return rating; }

    public void setId(Long id) { this.id = id; }
    public void setTourId(Long tourId) { this.tourId = tourId; }
    public void setDate(LocalDate date) { this.date.set(date); }
    public void setHour(int hour) { this.hour.set(String.valueOf(hour)); }
    public void setMinute(int minute) { this.minute.set(String.valueOf(minute)); }
    public void setComment(String comment) { this.comment.set(comment); }
    public void setDifficulty(String difficulty) { this.difficulty.set(difficulty); }
    public void setTotalDistance(String totalDistance) { this.totalDistance.set(totalDistance); }
    public void setTotalTime(String totalTime) { this.totalTime.set(totalTime); }
    public void setRating(int rating) { this.rating.set(rating); }

    public void setLog(Log log) {
        setId(log.getId());
        setTourId(log.getTourId());
        setDate(log.getDate());
        setHour(log.getHour());
        setMinute(log.getMinute());
        setComment(log.getComment());
        setDifficulty(String.valueOf(log.getDifficulty()));
        setTotalDistance(String.valueOf(log.getTotalDistance()));
        setTotalTime(String.valueOf(log.getTotalTime()));
        setRating(log.getRating());
    }

    public void clearLog() {
        setId(null);
        setTourId(null);
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
        setTourId(tour.getId());
    }

    public void saveLog() {
        LogDto logDto = new LogDto();

        logDto.setId(id);
        logDto.setTourId(tourId);

        // convert Date and Time into Timestamp
        LocalTime time = LocalTime.of(Integer.parseInt(hour.get()), Integer.parseInt(minute.get()));
        LocalDateTime dateTime = LocalDateTime.of(date.get(), time);
        logDto.setDatetime(Timestamp.valueOf(dateTime));

        logDto.setComment(comment.get());
        logDto.setDifficulty(difficulty.get());

        String distanceString = totalDistance.get();
        distanceString = distanceString.replace(",", ".").replace(" ", "");
        double distance;
        if (distanceString.endsWith("km") || distanceString.contains(".")) {
            distanceString = distanceString.replace("km", "");
            distance = Double.parseDouble(distanceString) * 1000;
        } else
            distance = Double.parseDouble(distanceString);
        logDto.setTotalDistance(String.valueOf(distance));

        String timeString = totalTime.get();
        String[] timeParts = timeString.split(":");
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        // Format is ss (seconds only)
        if (timeParts.length == 1)
            seconds = Integer.parseInt(timeParts[0]);
        // Format is mm:ss (minutes and seconds only)
        if (timeParts.length == 2) {
            minutes = Integer.parseInt(timeParts[0]);
            seconds = Integer.parseInt(timeParts[1]);
        } else if (timeParts.length == 3) {
            // Format is HH:mm:ss (hours, minutes, seconds)
            hours = Integer.parseInt(timeParts[0]);
            minutes = Integer.parseInt(timeParts[1]);
            seconds = Integer.parseInt(timeParts[2]);
        }
        int totalTimeInSeconds = hours * 3600 + minutes * 60 + seconds;
        logDto.setTotalDuration(String.valueOf(totalTimeInSeconds));
        logDto.setRating(rating.get());

        if (logDto.getId() == null)
            logService.saveLog(logDto);
        else
            logService.updateLog(logDto);
    }
}
