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
import org.telegrambot.demobot.services.handler.UpdateHandler;
import org.telegrambot.demobot.services.handler.UpdateHandlerFactory;

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
        log.info("Listener IS STARTED......");
        telegramBot.setUpdatesListener(updates -> {
            UpdateHandlerFactory handlerFactory = new UpdateHandlerFactory(accounting , messageHistory, botService, telegramBot);

            for (Update update : updates) {
                UpdateType updateType = checkType(update);
                log.info("TYPE IS: {} ..... UPDATE : {}", updateType, update);

                try {
                    UpdateHandler handler = handlerFactory.getHandler(updateType);
                    handler.handle(update);
                } catch (IllegalArgumentException e) {
                    log.info(e.getMessage());
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, TelegramListener::handleException, getUpdates);
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