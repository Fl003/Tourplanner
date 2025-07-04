package org.example.tourplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ImportExportService {
    private static final Logger logger = LogManager.getLogger();
    private static final String BASE_URL = "http://localhost:8080";
    private final ObjectMapper mapper = new ObjectMapper();

    public byte[] getTourExport(List<Long> tourIds) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/export");
            logger.info("send request for tour data export");
            String json = mapper.writeValueAsString(tourIds);

            request.setEntity(EntityBuilder.create()
                    .setText(json)
                    .setContentType(ContentType.create("application/json", StandardCharsets.UTF_8))
                    .build());

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            if (response.getCode() == 200) {
                logger.info("successful download of tour data export");
                return response.getEntity().getContent().readAllBytes();
            } else {
                logger.error("Failed to download tour data. Status: {} {}", response.getCode(), response.getReasonPhrase());
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception while downloading tour report tour data: {}", e.getMessage(), e);
            return null;
        }
    }

    public boolean importTourData(byte[] importBytes) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/import");
            logger.info("Sending tour data import request");

            request.setEntity(EntityBuilder.create()
                    .setBinary(importBytes)
                    .setContentType(ContentType.APPLICATION_JSON)
                    .build());

            ClassicHttpResponse response = client.executeOpen(null, request, null);
            if (response.getCode() == 200) {
                logger.info("Tour data import successful");
                return true;
            } else {
                logger.error("Import failed: {} {}", response.getCode(), response.getReasonPhrase());
                return false;
            }
        } catch (Exception e) {
            logger.error("Exception while importing tour data: {}", e.getMessage(), e);
            return false;
        }
    }
}
