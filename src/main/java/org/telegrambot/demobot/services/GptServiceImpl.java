package org.telegrambot.demobot.services;


import org.springframework.stereotype.Service;
import org.telegrambot.demobot.pojo.GptMessage;
import org.telegrambot.demobot.clients.ChatGPTClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Service
public class GptServiceImpl implements GptService {

    private final ChatGPTClient chatGptclient;
    Map<String, Function<String, String>> functionMap = new HashMap<>();


    public GptServiceImpl(ChatGPTClient chatGptclient) {
        this.chatGptclient = chatGptclient;
        functionMap.put("\uD83E\uDD14", this::getValidationResponse);
        functionMap.put("✍", this::getTranslate);
        functionMap.put("\uD83D\uDC40",this::getIdioms);
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
        messages.add(new GptMessage("system", "You are a Native American. When you receive a message, provide two to three idioms in english that correspond to the sentences as a short response and front of each write main sentence. Limit your response to no more than 10 lines. If a question is irrelevant or unclear, respond politely with: “I’m sorry, I cannot answer that question as it is outside my scope.”"));
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

    public Function<String, String> getFunction(String reaction) {
        System.out.println("Reaction: " + reaction);
        return functionMap.get(reaction);
    }

    public boolean checkEmojiHasPrompt(String emoji) {
        return functionMap.containsKey(emoji);
    }


}
