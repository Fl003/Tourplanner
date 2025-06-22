package org.example.tourplanner.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;

public class PdfService {
    private static final String BASE_URL = "http://localhost:8080";

    public byte[] getTourReportPdf(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/download/report/" + tourId);

            try (ClassicHttpResponse response = client.executeOpen(null, request, null)) {
                if (response.getCode() == 200) {
                    return response.getEntity().getContent().readAllBytes();
                } else {
                    System.err.println("Fehler beim Herunterladen der PDF: " +
                            response.getCode() + " " + response.getReasonPhrase());
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler beim PDF-Download: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getSummarizeReportPdf() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/download/report/");

            System.out.println(request.getUri());

            try (ClassicHttpResponse response = client.executeOpen(null, request, null)) {
                if (response.getCode() == 200) {
                    System.out.println("request successful");
                    return response.getEntity().getContent().readAllBytes();
                } else {
                    System.err.println("Fehler beim Herunterladen der PDF: " +
                            response.getCode() + " " + response.getReasonPhrase());
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("Fehler beim PDF-Download: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
