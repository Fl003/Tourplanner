package org.example.tourplanner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.example.tourplanner.dto.LogDto;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TourService {
    private static final Logger logger = LogManager.getLogger(TourService.class);
    private static final String BASE_URL = "http://localhost:8080";
    private final ObjectMapper mapper = new ObjectMapper();

    private final LogService logService;

    public TourService(LogService logService) {
        this.logService = logService;
    }

    public List<TourDto> getAllTours() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/tours");
            logger.info("send request for receiving tours");
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                logger.info("successful request");
                if (json != null) {
                    return mapper.readValue(json, new TypeReference<>() {});
                }
            } else {
                logger.warn("tour-call failed");
                //System.err.println(response.getCode() + " " + response.getReasonPhrase());
            }
        } catch (Exception e) {
                logger.error("failed to get tours", e);
        }
        return List.of();
    }

    public TourDto getTourById(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/tour/" + tourId);
            logger.info("send request for receiving tour by id {}", tourId);
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                logger.info("successful request");
                    return mapper.readValue(json, new TypeReference<>() {});
            } else {
                //System.err.println(response.getCode() + " " + response.getReasonPhrase());
                logger.warn("failed tour-call");
            }
        } catch (Exception e) {
            logger.error("failed to get tour by id {}", tourId, e);
        }
        return null;
    }

    public TourDto getLastCreatedTours() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/tour/latest");
            logger.info("send request for receiving tour last created");
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                logger.info("successful request");
                    return mapper.readValue(json, new TypeReference<>() {});
            } else {
                //System.err.println(response.getCode() + " " + response.getReasonPhrase());
                logger.warn("failed getLastcreatedTour-calls");
            }
        } catch (Exception e) {
            logger.error("failed to get tour last created", e);
        }
        return null;
    }

    public boolean saveTour(TourDto tour) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/tour");
            logger.info("send post request for saving tour {}", tour);
            String json = mapper.writeValueAsString(tour);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 201;
        } catch (Exception e) {
            logger.error("failed to save tour", e);
            return false;
        }
    }

    public boolean updateTour(TourDto tour) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(BASE_URL + "/tour");
            logger.info("send post request for updating tour {}", tour);
            String json = mapper.writeValueAsString(tour);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 201;
        } catch (Exception e) {
            logger.error("failed to update tour", e);
            return false;
        }
    }

    public boolean deleteTour(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + "/tour/" + tourId);
            logger.info("send delete request for receiving tour by id {}", tourId);
            ClassicHttpResponse response = client.executeOpen(null, request, null);
            if (response.getCode() == 200 || response.getCode() == 204) {
                logService.deleteLogsFromTourId(tourId);
                return true;
            }
        } catch (Exception e) {
            logger.error("failed to delete tour by id {}", tourId, e);
            return false;
        }
        return false;
    }

    public Tour convertTourDtoToTour(TourDto tourDto) {
        if (tourDto == null) return null;
        List<LogDto> logs = logService.getAllLogsForTourId(tourDto.getId());
        return new Tour(
                tourDto.getId(),
                tourDto.getName(),
                tourDto.getStartingPoint(),
                tourDto.getStartLat(),
                tourDto.getStartLng(),
                tourDto.getDestination(),
                tourDto.getDestinationLat(),
                tourDto.getDestinationLng(),
                TransportType.valueOf(tourDto.getTransportType()),
                tourDto.getDescription(),
                tourDto.getDistance(),
                tourDto.getDuration(),
                getPopularity(logs),
                getChildFriendliness(logs)
        );
    }

    public int getPopularity(List<LogDto> logs) {
        if (logs == null || logs.isEmpty()) return 5;

        double avgStars = 0;
        for (LogDto log : logs) {
            avgStars += log.getRating();
        }
        return (int) Math.floor(avgStars / logs.size());
    }

    public int getChildFriendliness(List<LogDto> logs) {
        // Difficulty: Easy = 2, Medium = 1, Difficult = 0
        // Total Time: <30min = 2, 30-90min = 1, >90min = 0
        // Distance: <5km = 2, 5-10km = 1, > 10km = 0
        // avg -> 1-5 stars
        if (logs == null || logs.size() == 0) return 1;
        int points = 0;
        for (LogDto log : logs) {
            switch(log.getDifficulty()) {
                case "Easy" -> points += 2;
                case "Medium" -> points += 1;
                default -> points += 0;
            }

            int time = log.getTotalDuration();
            if(time < 1800) points += 2;
            else if (time < 5400) points += 1;

            double distance = log.getTotalDistance();
            if (distance < 5000) points += 2;
            else if (distance < 10000) points += 1;
        }

        double avg = points / logs.size();
        return (int) Math.round(avg / 6 * 5);
    }

    public List<Tour> searchAllToursForString(String searchString) {
        List<Tour> filteredTours = new ArrayList<>();
        List<TourDto> tourDtos = getAllTours();
        for (TourDto tourDto : tourDtos) {
            Tour tour = convertTourDtoToTour(tourDto);
            if (searchInTour(tour, searchString)) {
                filteredTours.add(tour);
                continue;
            }
            List<LogDto> logs = logService.getAllLogsForTourId(tourDto.getId());
            if (!logs.isEmpty()) {
                if (logService.searchInLogs(logs, searchString)) {
                    filteredTours.add(tour);
                }
            }
        }
        return filteredTours;
    }

    private boolean searchInTour(Tour tour, String searchString) {
        if (tour.getName().contains(searchString)) return true;
        if (tour.getDescription().contains(searchString)) return true;
        if (tour.getDestination().contains(searchString)) return true;
        // if (tour.getDestinationLat().toString().contains(searchString)) return true;
        // if (tour.getDestinationLng().toString().contains(searchString)) return true;
        if (tour.getStartingpoint().contains(searchString)) return true;
        // if (tour.getStartLat().toString().contains(searchString)) return true;
        // if (tour.getStartLng().toString().contains(searchString)) return true;
        if (tour.getTransportType().toString().contains(searchString)) return true;
        String distanceString = tour.getDistance() / 1000 + "km";
        if (distanceString.contains(searchString)) return true;
        int hours = (int) Math.floor(tour.getEstimatedTime() / 3600);
        int minutes = (int) Math.floor((tour.getEstimatedTime() - (hours * 3600)) / 60);
        int seconds = (int) Math.floor((tour.getEstimatedTime() - (minutes * 60) - (hours * 3600)));
        String estimatedTime = hours + "h " + minutes + "min " + seconds + "sec";
        if (estimatedTime.contains(searchString)) return true;
        if (String.valueOf(tour.getPopularity()).contains(searchString)) return true;
        if (String.valueOf(tour.getChildFriendliness()).contains(searchString)) return true;
        return false;
    }
}
