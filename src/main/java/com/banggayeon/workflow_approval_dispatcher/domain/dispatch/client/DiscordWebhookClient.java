package com.banggayeon.workflow_approval_dispatcher.domain.dispatch.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class DiscordWebhookClient {
    private final RestTemplate rest = new RestTemplate();

    public void send(String webhookUrl, String content){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(Map.of("content", content), headers);
        rest.postForEntity(webhookUrl, req, String.class);
    }
}
