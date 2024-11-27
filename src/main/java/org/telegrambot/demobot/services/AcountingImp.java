package org.telegrambot.demobot.services;

import org.mapdb.DB;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcountingImp implements Accounting {


    private final HTreeMap<Long, String> chatMap;

    @Autowired
    public AcountingImp(DB db, HTreeMap<Long, String> chatMap) {
        this.chatMap = chatMap;
    }


    @Override
    public void addChatByJeus(long userId, long chatId) throws Exception {
        if (userId == 38672324) {
            if (chatMap.containsKey(chatId))
                throw new Exception("this chat already exists");
            chatMap.put(chatId, "add by jeus");
        } else {
            throw new Exception("The user doesn't have enough privileges");
        }
    }

    @Override
    public void deleteChatByJeus(long userId, long chatId) throws Exception {
        if (userId == 38672324) {
            if (!chatMap.containsKey(chatId))
                throw new Exception("this chat already not exists");
            chatMap.remove(chatId, "add by jeus");
        } else {
            throw new Exception("The user doesn't have enough privileges");
        }
    }

    @Override
    public void addChanelByToken(long userId, long chatId, String token) throws Exception {
        throw new Exception("This method is not implemented yet");
    }

    @Override
    public void checkValidation(long chatId) throws Exception{
        if (!chatMap.containsKey(chatId)) {
            throw new Exception("This chat doesn't exist");
        }
    }
}
