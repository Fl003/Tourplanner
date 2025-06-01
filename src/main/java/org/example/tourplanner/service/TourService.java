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
import org.example.tourplanner.dto.TourDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TourService {
    private static final String BASE_URL = "http://localhost:8080";
    private final ObjectMapper mapper = new ObjectMapper();

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
}
