package org.example.tourplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourDto {
    private Long id;
    private String name;
    private String description;
    public String startingPoint;
    public String destination;
    public String transportType;
    public Double distance;
    public Double duration;
}
