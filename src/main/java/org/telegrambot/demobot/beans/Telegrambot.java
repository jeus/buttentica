package org.telegrambot.demobot.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegrambot.demobot.clients.TelegramListener;
import org.telegrambot.demobot.services.Accounting;
import org.telegrambot.demobot.services.GptServiceImpl;
import org.telegrambot.demobot.services.MessageHistory;

@Configuration
public class Telegrambot {

    final MessageHistory messageHistory;
    final GptServiceImpl botService;
    final Accounting accounting;


    public Telegrambot(MessageHistory messageHistory, GptServiceImpl botService, Accounting accounting) {
        this.messageHistory = messageHistory;
        this.botService = botService;
        this.accounting = accounting;
    }


    @Bean
    public TelegramListener botClient() {
        return new TelegramListener(messageHistory, botService, accounting);
    }
}
