package org.telegrambot.demobot.services.handler;

import com.pengrad.telegrambot.TelegramBot;
import org.telegrambot.demobot.pojo.UpdateType;
import org.telegrambot.demobot.services.Accounting;
import org.telegrambot.demobot.services.GptServiceImpl;
import org.telegrambot.demobot.services.MessageHistory;

public class UpdateHandlerFactory {
    private final Accounting accounting;
    private final MessageHistory messageHistory;
    private final GptServiceImpl gptService;
    private final TelegramBot bot;
    public UpdateHandlerFactory(Accounting accounting, MessageHistory messageHistory, GptServiceImpl gptService, TelegramBot bot) {
        this.accounting = accounting;
        this.messageHistory = messageHistory;
        this.gptService = gptService;
        this.bot = bot;
    }

    public UpdateHandler getHandler(UpdateType updateType) {
        return switch (updateType) {
            case UpdateType.MESSAGE -> new MessageHandler(accounting, messageHistory);
            case UpdateType.MESSAGE_REACTION -> new MessageReactionHandler(accounting,messageHistory, gptService , bot);
            case UpdateType.MESSAGE_HASHTAG -> new HashTagHandler(accounting, messageHistory, bot);
            default -> throw new IllegalArgumentException("Undefined update type: " + updateType);
        };
    }
}