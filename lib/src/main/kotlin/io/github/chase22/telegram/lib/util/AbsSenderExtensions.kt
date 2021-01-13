package io.github.chase22.telegram.lib.util

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

fun AbsSender.send(text: String, chatId: String, replyToMessageId: Int? = null): Message {
    val sendMessageBuilder = SendMessage.builder()
            .chatId(chatId)
            .text(text)
    replyToMessageId?.let { sendMessageBuilder.replyToMessageId(replyToMessageId) }
    return this.execute(sendMessageBuilder.build())
}

fun AbsSender.send(text: String, chatId: Long, replyToMessageId: Int? = null): Message =
        this.send(text, chatId.toString(), replyToMessageId)
