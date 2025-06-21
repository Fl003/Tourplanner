package org.example.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.LogDto;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.LogService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class LogViewModel {
    private final LogService logService;
    private final ObservableList<Log> logList = FXCollections.observableArrayList();

    // selected Tour for tourId
    private Tour selectedTour;

    public LogViewModel(TourListViewModel tourListViewModel, LogService logService) {
        this.selectedTour = null;
        this.logService = logService;
        tourListViewModel.addSelectionChangedListener(this::setLogs);
    }

    public void setLogs(Tour tour) {
        // clear log table
        this.logList.clear();
        // return if no tour is selected
        if (tour == null) return;

        this.selectedTour = tour;
        // get all logs for tourId
        List<LogDto> logs = logService.getAllLogsForTourId(selectedTour.getId());
        // parse LogDto to Log
        for (LogDto logDto : logs) {
            // we have to split the Timestamp into Date and Hours, Minute
            LocalDateTime dateTime = logDto.getDatetime().toLocalDateTime();
            LocalDate date = dateTime.toLocalDate();
            LocalTime time = dateTime.toLocalTime();

            Log log = new Log(
                    logDto.getId(),
                    logDto.getTourId(),
                    date,
                    time.getHour(),
                    time.getMinute(),
                    logDto.getComment(),
                    logDto.getDifficulty(),
                    Integer.parseInt(logDto.getTotalDistance().replace(".", "")),
                    Integer.parseInt(logDto.getTotalDuration().replace(".", "")),
                    logDto.getRating()
            );
            // by adding to ObservableList, table displays new values
            logList.add(log);
        }
    }

    public ObservableList<Log> getLogs() {
        return logList;
    }

    public void deleteLog(Log log) {
        logService.deleteLog(log.getId());
        // refresh log table
        setLogs(selectedTour);
    }

    public Tour getSelectedTour() { return selectedTour; }
}
