package com.example.bajajqualifierjava.service;

import com.example.bajajqualifierjava.model.GenerateWebhookRequest;
import com.example.bajajqualifierjava.model.GenerateWebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.bajajqualifierjava.model.FinalQueryRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@Service
public class WebhookService {

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    private final RestTemplate restTemplate;

    private String webhookUrl;
    private String accessToken;

    @Autowired
    public WebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void initWebhook() {
        
        GenerateWebhookRequest request = new GenerateWebhookRequest(
                "Gowarni",
                "22BCE9480",
                "gowarni.22bce9480@vitapstudent.ac.in"
        );

        GenerateWebhookResponse response = restTemplate.postForObject(
                GENERATE_WEBHOOK_URL,
                request,
                GenerateWebhookResponse.class
        );

        if (response != null) {
            this.webhookUrl = response.getWebhook();
            this.accessToken = response.getAccessToken();

            System.out.println("=== Webhook initialized ===");
            System.out.println("Webhook URL   : " + webhookUrl);
            System.out.println("Access Token  : " + accessToken);
        } else {
            System.out.println("Failed to generate webhook: response is null");
        }
    }
    
    public void submitFinalQuery(String finalQuery) {
        if (webhookUrl == null || accessToken == null) {
            System.out.println("Webhook or accessToken is not initialized.");
            return;
        }

        FinalQueryRequest requestBody = new FinalQueryRequest(finalQuery);

        HttpHeaders headers = new HttpHeaders();
        // ⚠️ Assignment says: Authorization: <accessToken> (no 'Bearer ')
        headers.set("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(requestBody, headers);

        try {
            String response = restTemplate.postForObject(
                    webhookUrl,
                    entity,
                    String.class
            );

            System.out.println("=== Final query submitted ===");
            System.out.println("Request body : " + finalQuery);
            System.out.println("Response     : " + response);
        } catch (Exception e) {
            System.out.println("Error while submitting final query: " + e.getMessage());
        }
    }


    public String getWebhookUrl() {
        return webhookUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

