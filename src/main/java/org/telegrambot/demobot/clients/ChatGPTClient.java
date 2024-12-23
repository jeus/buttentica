package org.telegrambot.demobot.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.springframework.stereotype.Service;
import org.telegrambot.demobot.common.ChatGPTModel;
import org.telegrambot.demobot.common.GptCostCalculator;
import org.telegrambot.demobot.pojo.GptRequest;
import org.telegrambot.demobot.pojo.GptMessage;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ChatGPTClient {
    private static final String URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = System.getenv("OPENAI_API_KEY");// Replace with your actual API key

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ChatGPTModel model = ChatGPTModel.GPT_4O;


    public String askPrompt(List<GptMessage> messages) throws Exception {
        Request request = createGptRequest(messages);
        assert request != null;
        Response response = client.newCall(request).execute();
        return ResponseFormat(response);
    }


    private Request createGptRequest(List<GptMessage> messages) {

        float temperature = 1.0f;
        int max_tokens = 2048;
        float top_p = 1.0f;
        int frequency_penalty = 0;
        int presence_penalty = 0;
        GptRequest chat = new GptRequest(model.getApiName(), messages, temperature, max_tokens, top_p, frequency_penalty, presence_penalty);
        String jsonInput = null;
        try {
            jsonInput = mapper.writeValueAsString(chat);
        } catch (JsonProcessingException e) {
            log.error("Can't build chat: ", e);
            return null;
        }
        assert jsonInput != null;
        RequestBody body = RequestBody.create(jsonInput, MediaType.get("application/json; charset=utf-8"));
        return new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();
    }


    private String ResponseFormat(Response response) throws IOException {
        assert response.body() != null;
        String responseBody = response.body().string();
        JsonNode jsonNode = mapper.readTree(responseBody);
        JsonNode choicesNode = jsonNode.path("choices").get(0);
        JsonNode usage = jsonNode.path("usage");
        var cost = GptCostCalculator.costCalculator(model, usage.get("prompt_tokens").asInt(), usage.get("completion_tokens").asInt());
        JsonNode messageNode = choicesNode.path("message");
        var messageResponse = messageNode.path("content").asText() + "\nModel: " + model.getApiName() + " cost: " + String.format("%.5f", cost);
        log.info("messageResponse: {}", messageResponse);
        return messageResponse;
    }
}
