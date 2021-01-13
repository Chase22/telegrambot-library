package io.github.chase22.telegram.lib.util

import io.github.chase22.telegram.lib.ui.CallbackMessageService
import io.github.chase22.telegram.lib.ui.ConfirmableMessage
import io.github.chase22.telegram.lib.ui.ConfirmableMessage.ConfirmableMessagePermission.SENDER
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

fun sendConfirmableMessage(
        callbackMessageService: CallbackMessageService,
        text: String,
        replyToMessage: Message,
        permission: ConfirmableMessage.ConfirmableMessagePermission = SENDER
) {
    callbackMessageService.sendCallbackMessage(ConfirmableMessage(
            SendMessage.builder()
                    .chatId(replyToMessage.chatId.toString())
                    .replyToMessageId(replyToMessage.messageId)
                    .text(text)
                    .build(), permission))
}
