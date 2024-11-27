package org.telegrambot.demobot.services;

import jakarta.annotation.PreDestroy;
import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageHistory {

    private final DB db;
    private final HTreeMap<String, String> messagesMap;

    @Autowired
    public MessageHistory(DB db, HTreeMap<String, String> messagesMap) {
        this.db = db;
        this.messagesMap = messagesMap;
    }

    public void saveMessage(long messageId, long chatId, String message) {

        messagesMap.put(getId(messageId, chatId), message);
        db.commit(); // Save changes to the database
    }

    public String getMessage(long messageId, long chatId) {
        return messagesMap.get(getId(messageId, chatId));
    }

    private void removeAllMessages() {
         messagesMap.clear();
    }

    @PreDestroy
    public void close() {
        db.close(); // Close the database on application shutdown
    }

    private String getId(long messageId, long chatId) {
        return messageId + "_" + chatId;
    }

}
