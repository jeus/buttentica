package org.telegrambot.demobot.services;

/**
 * @author alikhandani
 * @created 26/11/2024
 * @project englishday
 */
public interface GptService {

    String getResponse(String message);
    String getValidationResponse(String message);
    String getIdioms(String message);
}
