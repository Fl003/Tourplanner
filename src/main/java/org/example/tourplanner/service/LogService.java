package org.example.tourplanner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
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
import org.example.tourplanner.model.Difficulty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogService {
    private static final Logger logger = LogManager.getLogger(LogService.class);
    private static final String BASE_URL = "http://localhost:8080";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<LogDto> getAllLogsForTourId(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/logs?tourId=" + tourId);
            logger.info("send request {}", request);
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                logger.info("Logs successfully requested for id {}", tourId);
                    return mapper.readValue(json, new TypeReference<>() {});
            } else {
                logger.warn("log-call failed {}", response);
            }
        } catch (Exception e) {
            logger.error("Failed to get logs", e);
        }
        return List.of();
    }

    public boolean saveLog(LogDto log) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/log");
            String json = mapper.writeValueAsString(log);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            logger.info("send request for saving a log {}", request);
            ClassicHttpResponse response = client.executeOpen(null, request, null);
            boolean success = response.getCode() == 200 || response.getCode() == 201;
            logger.info("Log saved {}", success ? "success" : "failed with HTTP" + response.getCode());
            return success;
        } catch (Exception e) {
            logger.error("Failed to save log", e);
            return false;
        }
    }

    public boolean updateLog(LogDto log) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(BASE_URL + "/log");
            String json = mapper.writeValueAsString(log);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            logger.info("send request for updating a log");
            ClassicHttpResponse response = client.executeOpen(null, request, null);
            boolean success = response.getCode() == 200 || response.getCode() == 201;
            logger.info("Log update: {}", success ? "success" : "failed with HTTP " + response.getCode());
            return success;
        } catch (Exception e) {
            logger.error("Failed to update log", e);
            return false;
        }
    }

    public boolean deleteLog(Long logId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + "/log/" + logId);
            logger.info("send request for deleting a log");
            ClassicHttpResponse response = client.executeOpen(null, request, null);
            logger.info("Log delete {}", logId);
            return response.getCode() == 200 || response.getCode() == 204;
        } catch (Exception e) {
            logger.error("Failed to delete log", e);
            return false;
        }
    }

    public boolean searchInLogs(List<LogDto> logs, String searchString) {
        logger.debug("search for ... in logs {}", searchString);
        for (LogDto log : logs) {
            LocalDateTime dateTime = log.getDatetime().toLocalDateTime();
            LocalDate date = dateTime.toLocalDate();
            LocalTime time = dateTime.toLocalTime();
            String dateTimeString = String.format("%s %02d:%02d", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), time.getHour(), time.getMinute());
            if (dateTimeString.contains(searchString)) return true;
            if (log.getComment().contains(searchString)) return true;
            if (log.getDifficulty().contains(searchString)) return true;
            String distanceString = log.getTotalDistance() / 1000 + "km";
            if (distanceString.contains(searchString)) return true;
            int totalTime = log.getTotalDuration();
            int hour = totalTime / 3600;
            int minute = totalTime % 3600 / 60;
            int second = totalTime % 60;
            String timeString = "";
            if (hour != 0)
                timeString += hour + "h ";
            timeString += minute + "min " + second + "sec";
            if (timeString.contains(searchString)) return true;
            if (String.valueOf(log.getRating()).contains(searchString)) return true;
        }
        return false;
    }


}
