package org.example.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.LogDto;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.LogService;
import org.example.tourplanner.service.TourService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class LogViewModel {
    private final LogService logService;
    private final TourService tourService;
    private final TourListViewModel tourListViewModel;
    private final ObservableList<Log> logList = FXCollections.observableArrayList();

    // selected Tour for tourId
    private Tour selectedTour;

    public LogViewModel(TourListViewModel tourListViewModel, LogService logService, TourService tourService) {
        this.selectedTour = null;
        this.logService = logService;
        this.tourService = tourService;
        this.tourListViewModel = tourListViewModel;
        tourListViewModel.addSelectionChangedListener(this::setLogs);
    }

    public void setLogs(Long tourId) {
        // clear log table
        this.logList.clear();
        // return if no tour is selected
        if (tourId == null) return;

        this.selectedTour = tourService.convertTourDtoToTour(tourService.getTourById(tourId));
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
                    logDto.getTotalDistance(),
                    logDto.getTotalDuration(),
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
        tourListViewModel.reloadTour(selectedTour.getId());
    }

    public Tour getSelectedTour() { return selectedTour; }
}
