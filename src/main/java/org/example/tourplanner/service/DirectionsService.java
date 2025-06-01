package org.example.tourplanner.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class DirectionsService {
    private static final String BASE_URL = "https://api.openrouteservice.org/v2/directions/";

    public String getDirections(String transport, Double startLat, Double startLng, Double endLat, Double endLng) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = BASE_URL + transport;
            url += "?api_key=5b3ce3597851110001cf6248128a6e3707124b69a94712fdc871147c";
            url += "&start=" + startLat + "," + startLng;
            url += "&end=" + endLat + "," + endLng;
            System.out.println("DirectionsService: " + url);
            HttpGet request = new HttpGet(url);
            ClassicHttpResponse response = client.executeOpen(null, request, null);

            if (response.getCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                System.err.println(response.getCode() + " " + response.getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
