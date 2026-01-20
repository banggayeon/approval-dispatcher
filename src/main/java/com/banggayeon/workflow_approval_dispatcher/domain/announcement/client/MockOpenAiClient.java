package com.banggayeon.workflow_approval_dispatcher.domain.announcement.client;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockOpenAiClient implements OpenAiClient {
    @Override
    public GeneratedAnnouncement generate(String templatePromptBody, List<String> keywords, String tone){
        String t = (tone == null || tone.isBlank()) ? "default" : tone;
        String kw = (keywords == null || keywords.isEmpty()) ? "(none)" : String.join(",", keywords);

        String title = "[Mock] 공지: " + kw;
        String body = """
                (tone=%s)
                키워드: %s

                %s
                """.formatted(t, kw, templatePromptBody);

        return new GeneratedAnnouncement(title, body);
    }
}
