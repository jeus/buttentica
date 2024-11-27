package org.telegrambot.demobot.clients;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.reaction.ReactionTypeEmoji;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.UpdatesListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegrambot.demobot.pojo.UpdateType;
import org.telegrambot.demobot.services.Accounting;
import org.telegrambot.demobot.services.GptServiceImpl;
import org.telegrambot.demobot.services.MessageHistory;

import java.util.Arrays;
import java.util.Optional;


public class TelegramListener {
    private final Logger log = LoggerFactory.getLogger(TelegramListener.class);
    private final String botToken = System.getenv("TELEGRAM_BOT_TOKEN");
    TelegramBot telegramBot = new TelegramBot(botToken);
    private final String[] allowedUpdate = {
            "message",
            "edited_message",
//            "channel_post",
//            "edited_channel_post",
//            "inline_query",
//            "chosen_inline_result",
//            "callback_query",
//            "shipping_query",
//            "pre_checkout_query",
//            "poll",
//            "poll_answer",
//            "my_chat_member",
//            "chat_member",
//            "chat_join_request",
            "message_reaction",
            "message_reaction_count"};
    private final MessageHistory messageHistory;
    private final GptServiceImpl botService;
    private final Accounting accounting;

    public TelegramListener(MessageHistory messageHistory, GptServiceImpl botService, Accounting accounting) {
        this.messageHistory = messageHistory;
        this.botService = botService;
        this.accounting = accounting;
        this.setListener();
    }

    private void setListener() {
        GetUpdates getUpdates = new GetUpdates();
        getUpdates.allowedUpdates(allowedUpdate);
//        getUpdates.allowedUpdates("message", "edited_message","my_chat_member", "chat_member");
        log.info(">>> Listener IS ADDED......");

        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                UpdateType updateType = checkType(update);
                log.info("TYPE IS: {} ..... UPDATE : {}", updateType, update);
                switch (updateType) {
                    case UpdateType.MESSAGE:
                        try {
                            accounting.checkValidation(update.message().chat().id());
                            newMessage(update);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        break;
                    case UpdateType.MESSAGE_REACTION:
                        try {
                            accounting.checkValidation(update.messageReaction().chat().id());
                            newReaction(update);
                        }catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        break;
                    case UpdateType.MESSAGE_HASHTAG:
                        hashTagCommand(update);
                        break;
                    case UpdateType.MESSAGE_BOT_COMMAND:
                        break;
                    default:
                        log.info("Undefined update type: {}", updateType);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, TelegramListener::handleException, getUpdates);
    }

    private void newMessage(Update update) {

        long chatId = update.message().chat().id();
        var messageId = update.message().messageId();
        String messageText = update.message().text();
        log.info("SAVE THIS MESSAGE {}_{} .....{} ", messageId, chatId, messageText);
        messageHistory.saveMessage(messageId, chatId, messageText);
    }


    private void hashTagCommand(Update update) {
        if (update.message().text().equals("#ADD_CHAT")) {
            var chatId = update.message().chat().id();
            try {
                accounting.addChatByJeus(update.message().from().id(), update.message().chat().id());
                telegramBot.execute(new SendMessage(chatId, "chat permission issued"));
            } catch (Exception e) {
                telegramBot.execute(new SendMessage(chatId, e.getMessage()));
                log.error(e.getMessage());
            }
        } else if (update.message().text().equals("#DELETE_CHAT")) {
            var chatId = update.message().chat().id();
            try {
                accounting.deleteChatByJeus(update.message().from().id(), update.message().chat().id());
                telegramBot.execute(new SendMessage(chatId, "chat permission terminated"));
            } catch (Exception e) {
                telegramBot.execute(new SendMessage(chatId, e.getMessage()));
                log.error(e.getMessage());
            }
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

            SendResponse response = telegramBot.execute(new SendMessage(chatId, openAIResponse));

            if (response.isOk()) {
                log.info("Message sent successfully");
            } else {
                log.info("Failed to send message:{} ", response.description());
            }
        }

    }

    private UpdateType checkType(Update update) {

        if (update.message() != null && update.message().text() != null) {
            if (update.message().entities() != null && update.message().entities().length != 0) {
                var messageType = update.message().entities()[0].type().toString();
                if (messageType.equals("bot_command")) {
                    return UpdateType.MESSAGE_BOT_COMMAND;
                } else if (messageType.equals("hashtag")) {
                    return UpdateType.MESSAGE_HASHTAG;
                }
            }
            return UpdateType.MESSAGE;
        }
        if (update.messageReaction() != null) {
            return UpdateType.MESSAGE_REACTION;
        }
        return UpdateType.UNDEFINED;
    }

    private static void handleException(TelegramException e) {
        if (e.response() != null) {
            // Got bad response from Telegram
            System.err.println("Error code: " + e.response().errorCode());
            System.err.println("Description: " + e.response().description());
        } else {
            e.printStackTrace();
        }
    }
}