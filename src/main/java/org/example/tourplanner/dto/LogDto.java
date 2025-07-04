package org.example.tourplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    private Long id;
    private Long tourId;
    private Timestamp datetime;
    private String comment;
    private String difficulty;
    private Double totalDistance;
    private Integer totalDuration;
    private Integer rating;
}
