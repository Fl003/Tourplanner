package org.example.tourplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GeocodeService {
    private static final Logger logger = LogManager.getLogger(GeocodeService.class);
    private static final String BASE_URL = "https://api.openrouteservice.org/geocode/search";

    public String getCoordinates(String location) {
            String url = BASE_URL + "?api_key=5b3ce3597851110001cf6248128a6e3707124b69a94712fdc871147c";
            url += "&text=" + location;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            logger.info("Send geocode-request {}", location);
            HttpGet request = new HttpGet(url);
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            int statusCode = response.getCode();
            if (statusCode == 200) {
                logger.info("Geocode-request successful: HTTP {}", statusCode);
                return EntityUtils.toString(response.getEntity());
            } else {
                logger.warn("Geocode-request failed: HTTP {}", statusCode);
                //System.err.println(response.getCode() + " " + response.getReasonPhrase());
            }
        } catch (Exception e) {
            logger.error("Geocode-request failed", e);
        }
        return null;
    }
}
