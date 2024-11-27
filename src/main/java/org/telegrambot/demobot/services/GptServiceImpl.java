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

        return chatGptclient.sendRequest(messages);
    }

    public String getIdioms(String message) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", "You are an native english man who is living in united state you get message and tell me idioms corresponding to sentences as a short response two or at most 3 idioms are enough for any sentence."));
        messages.add(new GptMessage("user", message));

        return chatGptclient.sendRequest(messages);
    }


    public String getValidationResponse(String message) {
        List<GptMessage> messages = new ArrayList<>();
        messages.add(new GptMessage("system", "You are a scientific validator you get message from user and validate it is correct or not."));
        messages.add(new GptMessage("user", message));

        return chatGptclient.sendRequest(messages);
    }


}
