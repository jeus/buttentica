package org.telegrambot.demobot.services.handler;


import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;

@Service
public interface UpdateHandler {
    void handle(Update update);
}
