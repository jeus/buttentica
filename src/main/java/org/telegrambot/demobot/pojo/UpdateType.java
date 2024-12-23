package org.telegrambot.demobot.pojo;

public enum UpdateType {
    MESSAGE("message"),
    MESSAGE_BOT_COMMAND("message_bot_command"),
    MESSAGE_HASHTAG("message_hashtag"),
    EDITED_MESSAGE("edited_message"),
    CHANNEL_POST("channel_post"),
    EDITED_CHANNEL_POST("edited_channel_post"),
    INLINE_QUERY("inline_query"),
    CHOSEN_INLINE_RESULT("chosen_inline_result"),
    CALLBACK_QUERY("callback_query"),
    SHIPPING_QUERY("shipping_query"),
    PRE_CHECKOUT_QUERY("pre_checkout_query"),
    POLL("poll"),
    POLL_ANSWER("poll_answer"),
    MY_CHAT_MEMBER("my_chat_member"),
    CHAT_MEMBER("chat_member"),
    CHAT_JOIN_REQUEST("chat_join_request"),
    MESSAGE_REACTION("message_reaction"),
    MESSAGE_REACTION_COUNT("message_reaction_count"),
    UNDEFINED("undefined");
    private final String name;

    UpdateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
