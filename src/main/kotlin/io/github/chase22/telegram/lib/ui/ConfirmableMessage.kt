package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import io.github.chase22.telegram.lib.callback.CallbackMessage
import io.github.chase22.telegram.lib.ui.ConfirmableMessage.ConfirmableMessagePermission.*
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

class ConfirmableMessage(
        private val sendMessage: SendMessage,
        private val permission: ConfirmableMessagePermission = SENDER
) : CallbackMessage {
    override fun processCallback(absSender: AbsSender, callbackQuery: CallbackQuery, adminService: GroupAdminService): Boolean {
        val message = callbackQuery.message
        if (isPermitted(callbackQuery, adminService)) {
            absSender.execute(AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.id)
                    .text("Message confirmed")
                    .build()
            )
            absSender.execute(DeleteMessage(message.chatId.toString(), message.messageId))
            return false
        }
        absSender.execute(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.id)
                .text("Action is not allowed")
                .build()
        )
        return true
    }

    override fun getSendMessage(): SendMessage {
        sendMessage.replyMarkup = InlineKeyboardMarkup
                .builder()
                .keyboardRow(listOf(InlineKeyboardButton.builder()
                        .text("Confirm")
                        .callbackData("confirm")
                        .build()
                ))
                .build()
        return sendMessage
    }

    private fun isPermitted(callbackQuery: CallbackQuery, adminService: GroupAdminService): Boolean {
        val userId = callbackQuery.from.id
        return when (permission) {
            SENDER -> userId == callbackQuery.message.replyToMessage.from.id
            ADMIN -> adminService.isAdmin(callbackQuery.message.chatId, userId)
            EVERYONE -> true
        }
    }

    enum class ConfirmableMessagePermission {
        SENDER, ADMIN, EVERYONE
    }
}
