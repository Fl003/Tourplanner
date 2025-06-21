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

import java.nio.charset.StandardCharsets;
import java.util.List;

public class LogService {
    private static final String BASE_URL = "http://localhost:8080";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<LogDto> getAllLogsForTourId(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/logs?tourId=" + tourId);
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

    public boolean saveLog(LogDto log) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/log");
            String json = mapper.writeValueAsString(log);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            System.out.println(json);

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
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

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLog(Long logId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + "/log/" + logId);

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            return response.getCode() == 200 || response.getCode() == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
