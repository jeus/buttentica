package org.telegrambot.demobot.services;

public interface Accounting {

     void addChatByJeus(long userId , long chatId) throws Exception;

     void addChanelByToken(long userId , long chatId , String token) throws Exception;
     void deleteChatByJeus(long userId, long chatId) throws Exception;

     void checkValidation(long chatId) throws Exception;
}
