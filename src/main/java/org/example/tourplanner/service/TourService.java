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

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TourService {
    private static final String BASE_URL = "http://localhost:8080";
    private final ObjectMapper mapper = new ObjectMapper();

    private final LogService logService;

    public TourService(LogService logService) {
        this.logService = logService;
    }

    public List<TourDto> getAllTours() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/tours");
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());

                if (json != null) {
                    return mapper.readValue(json, new TypeReference<>() {});
                }
            } else {
                System.err.println(response.getCode() + " " + response.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public TourDto getTourById(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/tour/" + tourId);
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());

                if (json != null) {
                    return mapper.readValue(json, new TypeReference<>() {});
                }
            } else {
                System.err.println(response.getCode() + " " + response.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TourDto getLastCreatedTours() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/tour/latest");
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());

                if (json != null) {
                    return mapper.readValue(json, new TypeReference<>() {});
                }
            } else {
                System.err.println(response.getCode() + " " + response.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveTour(TourDto tour) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/tour");
            String json = mapper.writeValueAsString(tour);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTour(TourDto tour) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(BASE_URL + "/tour");
            String json = mapper.writeValueAsString(tour);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTour(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + "/tour/" + tourId);

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        // count: <=1, <=3, <=6, <=10, ->
        // stars:  1,   2,   3,    4,   5
        if (logs == null) return 1;
        int logsCount = logs.size();
        if (logsCount <= 1) return 1;
        if (logsCount <= 3) return 2;
        if (logsCount <= 6) return 3;
        if (logsCount <= 10) return 4;
        return 5;
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
}
