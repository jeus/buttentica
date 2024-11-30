package org.telegrambot.demobot.services.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.reaction.ReactionTypeEmoji;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.telegrambot.demobot.services.Accounting;
import org.telegrambot.demobot.services.GptServiceImpl;
import org.telegrambot.demobot.services.MessageHistory;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class MessageReactionHandler implements UpdateHandler {
    private final Accounting accounting;
    private final MessageHistory messageHistory;
    private final GptServiceImpl botService;
    private final TelegramBot bot;
    public MessageReactionHandler(Accounting accounting, MessageHistory messageHistory,GptServiceImpl botService,  TelegramBot bot) {
        this.accounting = accounting;
        this.messageHistory = messageHistory;
        this.botService = botService;
        this.bot = bot;
    }

    @Override
    public void handle(Update update) {
        try {
            accounting.checkValidation(update.messageReaction().chat().id());
            newReaction(update);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void newReaction(Update update) {
        Optional<ReactionTypeEmoji> firstMessage = Arrays.stream(update.messageReaction().newReaction()).filter(rectionType -> "emoji".equals(rectionType.type())).map(ReactionTypeEmoji.class::cast).filter(reactionTypeEmoji -> "\uD83E\uDD14".equals(reactionTypeEmoji.emoji())).findFirst();
        if (firstMessage.isPresent()) {
            long messageId = update.messageReaction().messageId();
            long chatId = update.messageReaction().chat().id();
            String messageText = messageHistory.getMessage(messageId, chatId);
            log.info("RECEIVE THIS MESSAGE {}    {}_{} .....{} ", firstMessage.get(), messageId, chatId, messageText);
            String openAIResponse = botService.getValidationResponse(messageText);

            SendResponse response = bot.execute(new SendMessage(chatId, openAIResponse));

            if (response.isOk()) {
                log.info("Message sent successfully");
            } else {
                log.info("Failed to send message:{} ", response.description());
            }
        }

    }
}