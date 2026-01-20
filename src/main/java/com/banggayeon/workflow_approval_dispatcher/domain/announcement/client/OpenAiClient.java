package com.banggayeon.workflow_approval_dispatcher.domain.announcement.client;

import java.util.List;

public interface OpenAiClient {
    GeneratedAnnouncement generate(String templatePromptBody, List<String> keywords, String tone);
}
