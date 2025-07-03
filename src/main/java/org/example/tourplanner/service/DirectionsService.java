package org.example.tourplanner.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DirectionsService {
    private static final Logger logger = LogManager.getLogger(DirectionsService.class);
    private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions/";

    public String getDirections(String transport, Double startLat, Double startLng, Double endLat, Double endLng) {
        String url = BASE_URL + transport;
        url += "?api_key=5b3ce3597851110001cf6248128a6e3707124b69a94712fdc871147c";
        url += "&start=" + startLat + "," + startLng;
        url += "&end=" + endLat + "," + endLng;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            logger.info("Send request to URL: " + url);

            HttpGet request = new HttpGet(url);
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            int statusCode = response.getCode();
            if (statusCode == 200) {
                logger.info("Request successful HTTP {}", statusCode);
                return EntityUtils.toString(response.getEntity());
            } else {
                logger.warn("Request failed: {}", statusCode);
                //System.err.println(response.getCode() + " " + response.getReasonPhrase());
            }
        } catch (Exception e) {
            logger.error("Directions - Call failed", e);
        }
        return null;
    }
}
