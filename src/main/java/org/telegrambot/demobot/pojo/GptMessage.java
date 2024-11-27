package org.telegrambot.demobot.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GptMessage {
    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;

    //Constructor
    public GptMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
