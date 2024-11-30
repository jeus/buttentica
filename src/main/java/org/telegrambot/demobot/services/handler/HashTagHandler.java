package org.telegrambot.demobot.services.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.telegrambot.demobot.services.Accounting;
import org.telegrambot.demobot.services.MessageHistory;
@Slf4j
public class HashTagHandler implements UpdateHandler {


    private final Accounting accounting;
    private final MessageHistory messageHistory;
    private final TelegramBot bot;
    public HashTagHandler(Accounting accounting, MessageHistory messageHistory , TelegramBot bot) {
        this.accounting = accounting;
        this.messageHistory = messageHistory;
        this.bot = bot;
    }


    @Override
    public void handle(Update update) {
        // Handle hashtag command
        hashTagCommand(update);
    }


    private void hashTagCommand(Update update) {
        if (update.message().text().equals("#ADD_CHAT")) {
            var chatId = update.message().chat().id();
            try {
                accounting.addChatByJeus(update.message().from().id(), update.message().chat().id());
                bot.execute(new SendMessage(chatId, "chat permission issued"));
            } catch (Exception e) {
                bot.execute(new SendMessage(chatId, e.getMessage()));
                log.error(e.getMessage());
            }
        } else if (update.message().text().equals("#DELETE_CHAT")) {
            var chatId = update.message().chat().id();
            try {
                accounting.deleteChatByJeus(update.message().from().id(), update.message().chat().id());
                bot.execute(new SendMessage(chatId, "chat permission terminated"));
            } catch (Exception e) {
                bot.execute(new SendMessage(chatId, e.getMessage()));
                log.error(e.getMessage());
            }
        }
    }
}