package org.telegrambot.demobot.services.handler;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.telegrambot.demobot.services.Accounting;
import org.telegrambot.demobot.services.MessageHistory;

@Slf4j
public class MessageHandler implements UpdateHandler {

    private final Accounting accounting;
    private final MessageHistory messageHistory;
    public MessageHandler(Accounting accounting, MessageHistory messageHistory) {
        this.accounting = accounting;
        this.messageHistory = messageHistory;
    }

    @Override
    public void handle(Update update) {
        try {
            accounting.checkValidation(update.message().chat().id());
            newMessage(update);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }



    private void newMessage(Update update) {

        long chatId = update.message().chat().id();
        var messageId = update.message().messageId();
        String messageText = update.message().text();
        log.info("SAVE THIS MESSAGE {}_{} .....{} ", messageId, chatId, messageText);
        messageHistory.saveMessage(messageId, chatId, messageText);
    }

}
