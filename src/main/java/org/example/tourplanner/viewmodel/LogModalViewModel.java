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
    private final StringProperty difficulty = new SimpleStringProperty("Easy");
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
    public void setTotalDistance(Double totalDistance) {
        // convert double meter value into km value
        double km = totalDistance / 1000;
        this.totalDistance.set(km + "km");
    }
    public void setTotalTime(int totalTime) {
        // convert int time into hh::mm::ss
        int hour = totalTime / 3600;
        int minute = totalTime % 3600 / 60;
        int second = totalTime % 60;
        String timeString = "";
        if (hour != 0)
            timeString += hour + "h ";
        timeString += minute + "min " + second + "sec";
        this.totalTime.set(timeString);
    }
    public void setRating(int rating) { this.rating.set(rating); }

    public void setLog(Log log) {
        setId(log.getId());
        setTourId(log.getTourId());
        setDate(log.getDate());
        setHour(log.getHour());
        setMinute(log.getMinute());
        setComment(log.getComment());
        setDifficulty(String.valueOf(log.getDifficulty()));
        setTotalDistance(log.getTotalDistance());
        setTotalTime(log.getTotalTime());
        setRating(log.getRating());
    }

    public void clearLog() {
        setId(null);
        setTourId(null);
        date.set(null);
        hour.set(null);
        minute.set(null);
        comment.set(null);
        difficulty.set("Easy");
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
        logDto.setTotalDistance(getDistanceFromString(totalDistance.get()));
        logDto.setTotalDuration(getSecondsFromString(totalTime.get()));
        logDto.setRating(rating.get());

        if (logDto.getId() == null)
            logService.saveLog(logDto);
        else
            logService.updateLog(logDto);
    }

    private double getDistanceFromString(String distanceString) {
        distanceString = distanceString.replace(",", ".").replace(" ", "");
        double distance;
        // format is 11,32km or 11,32
        if (distanceString.endsWith("km") || distanceString.contains(".")) {
            distanceString = distanceString.replace("km", "");
            distance = Double.parseDouble(distanceString) * 1000;
        } else
            // format is 11320 or 11320m
            distance = Double.parseDouble(distanceString.replace("m", ""));
        return distance;
    }

    private int getSecondsFromString(String timeString) {
        // format is sec or mm:ss or hh:mm:ss
        if (timeString.contains(":")) {
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
            return hours * 3600 + minutes * 60 + seconds;
        } else {
            // format is 43sec or 23min 43sec or 2h 23min 43sec
            int totalTimeInSeconds = 0;
            String[] timeParts = timeString.split(" ");
            for (String timePart : timeParts) {
                if (timePart.endsWith("h")) {
                    String hours = timePart.substring(0, timePart.length() - 1);
                    totalTimeInSeconds += Integer.parseInt(hours) * 3600;
                } else if (timePart.endsWith("min")) {
                    String minutes = timePart.substring(0, timePart.length() - 3);
                    totalTimeInSeconds += Integer.parseInt(minutes) * 60;
                } else {
                    String sec = timePart.substring(0, timePart.length() - 3);
                    totalTimeInSeconds += Integer.parseInt(sec);
                }
            }
            return totalTimeInSeconds;
        }
    }
}
