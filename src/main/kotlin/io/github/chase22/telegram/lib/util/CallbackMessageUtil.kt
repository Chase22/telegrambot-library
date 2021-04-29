package io.github.chase22.telegram.lib.util

import io.github.chase22.telegram.lib.callback.CallbackMessageService
import io.github.chase22.telegram.lib.ui.ConfirmableMessageHandler
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

fun sendConfirmableMessage(
    callbackMessageService: CallbackMessageService,
    text: String,
    replyToMessage: Message,
) {
    callbackMessageService.sendCallbackMessage(
        ConfirmableMessageHandler.createCallbackMessage(
            SendMessage.builder()
                .chatId(replyToMessage.chatId.toString())
                .replyToMessageId(replyToMessage.messageId)
                .text(text)
                .build()
        )
    )
}
