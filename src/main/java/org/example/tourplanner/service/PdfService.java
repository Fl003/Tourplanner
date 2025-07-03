package org.example.tourplanner.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PdfService {
    private static final Logger logger = LogManager.getLogger();
    private static final String BASE_URL = "http://localhost:8080";

    public byte[] getTourReportPdf(Long tourId) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/download/report/" + tourId);
            logger.info("send request for download report");
            try (ClassicHttpResponse response = client.executeOpen(null, request, null)) {
                if (response.getCode() == 200) {
                    logger.info("successful download of tour report");
                    return response.getEntity().getContent().readAllBytes();
                } else {
                    logger.error("Failed to download PDF. Status: {} {}", response.getCode(), response.getReasonPhrase());
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Exception while downloading tour report PDF: {}", e.getMessage(), e);
            return null;

        }
    }

    public byte[] getSummarizeReportPdf() {
        logger.info("send request for summarize report");
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/download/report/");
            logger.debug("request URL: {}", request.getUri().toString());

            try (ClassicHttpResponse response = client.executeOpen(null, request, null)) {
                if (response.getCode() == 200) {
                    logger.info("Successfully downloaded summary report");
                    return response.getEntity().getContent().readAllBytes();
                } else {
                    logger.error("Failed to download summary PDF. Status: {} {}", response.getCode(), response.getReasonPhrase());
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Exception while downloading summary report PDF: {}", e.getMessage(), e);
            return null;
        }
    }
}
