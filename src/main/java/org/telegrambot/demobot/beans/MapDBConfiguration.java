package org.telegrambot.demobot.beans;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MapDBConfiguration {
    private final Logger log = LoggerFactory.getLogger(MapDBConfiguration.class);
    private static final String DB_PATH = System.getenv("MESSAGE_HISTORY_PATH");

    @Bean
    public DB db() {
        var db = DBMaker.fileDB(DB_PATH).fileMmapEnable().make();
        return db;
    }

    @Bean
    public HTreeMap<String, String> messagesMap(DB db) {
        // Create or load the messages map
        var hashMap = db.hashMap("messages", Serializer.STRING, Serializer.STRING).createOrOpen();
        log.info("Messages hashMap opened, SIZE:{}", hashMap.size());
        return hashMap;
    }


    @Bean
    public HTreeMap<Long, String> chatMap(DB db) {
        HTreeMap<Long, String> hashMap = db.hashMap("chat", Serializer.LONG, Serializer.STRING).createOrOpen();
        log.info("Messages hashMap opened, SIZE:{}", hashMap.size());
        return hashMap;
    }
}