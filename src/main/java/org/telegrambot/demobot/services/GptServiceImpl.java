package org.telegrambot.demobot.services;


import org.springframework.stereotype.Service;
import org.telegrambot.demobot.pojo.GptMessage;
import org.telegrambot.demobot.clients.ChatGPTClient;

import java.util.ArrayList;
import java.util.List;


@Service
public class GptServiceImpl implements GptService {

    private final ChatGPTClient chatGptclient;

    public GptServiceImpl(ChatGPTClient chatGptclient) {
        this.chatGptclient = chatGptclient;
    }


    public String getResponse(String message) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", "You are an English Language Specialists which holds a Ph. D. in TESOL and teaches IELTS."));
        messages.add(new GptMessage("user", message));

        try {
            return chatGptclient.askPrompt(messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getIdioms(String message) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", "You are an native english man who is living in united state you get message and tell me idioms corresponding to sentences as a short response two or at most 3 idioms are enough for any sentence."));
        messages.add(new GptMessage("user", message));

        try {
            return chatGptclient.askPrompt(messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getTranslate(String message) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", "You are a translator. Check the user message and detect if the text is in English; if it is, translate it to Persian. Otherwise, if it is in Persian, translate it to English"));
        messages.add(new GptMessage("user", message));

        try {
            return chatGptclient.askPrompt(messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getValidationResponse(String message) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", "You are a scientific validator you get message from user and validate it is correct or not. Respond to questions with no more than 5 lines. If a question is irrelevant or unclear, respond politely with: “I’m sorry, I cannot answer that question as it is outside my scope.”"));
        messages.add(new GptMessage("user", message));

        try {
            return chatGptclient.askPrompt(messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
