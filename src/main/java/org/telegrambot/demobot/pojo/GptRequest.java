package org.telegrambot.demobot.pojo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
// Helper class to represent the Chat object
public class GptRequest {
    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<GptMessage> messages;

    @JsonProperty("temperature")
    private float temperature;

    @JsonProperty("max_tokens")
    private int max_tokens;

    @JsonProperty("top_p")
    private float top_p;

    @JsonProperty("frequency_penalty")
    private int frequency_penalty;

    @JsonProperty("presence_penalty")
    private int presence_penalty;

    public GptRequest(String model, List<GptMessage> messages, float temperature, int max_tokens, float top_p, int frequency_penalty, int presence_penalty) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
        this.top_p = top_p;
        this.frequency_penalty = frequency_penalty;
        this.presence_penalty = presence_penalty;
    }

}