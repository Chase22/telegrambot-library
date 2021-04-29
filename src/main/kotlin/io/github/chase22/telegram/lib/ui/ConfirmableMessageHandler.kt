package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import io.github.chase22.telegram.lib.callback.CallbackMessage
import io.github.chase22.telegram.lib.callback.CallbackMessageHandler
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender
import javax.inject.Singleton

@Singleton
open class ConfirmableMessageHandler(
    private val adminService: GroupAdminService,
    private val absSender: AbsSender
) : CallbackMessageHandler {
    override val identifier = IDENTIFIER

    protected open fun isPermitted(callbackQuery: CallbackQuery, adminService: GroupAdminService): Boolean {
        val userId = callbackQuery.from.id

        return adminService.isAdmin(
            callbackQuery.message.chatId,
            userId
        ) || userId == callbackQuery.message.replyToMessage.from.id
    }

    override fun processUpdate(callbackQuery: CallbackQuery): Boolean {
        val message = callbackQuery.message
        if (isPermitted(callbackQuery, adminService)) {
            absSender.execute(
                AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackQuery.id)
                    .text("Message confirmed")
                    .build()
            )
            absSender.execute(DeleteMessage(message.chatId.toString(), message.messageId))
            return false
        }
        absSender.execute(
            AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQuery.id)
                .text("Action is not allowed")
                .build()
        )
        return true
    }

    companion object {
        const val IDENTIFIER = "CONFIRMABLE_MESSAGE"

        fun createCallbackMessage(sendMessage: SendMessage): CallbackMessage {
            sendMessage.replyMarkup = InlineKeyboardMarkup
                .builder()
                .keyboardRow(
                    listOf(
                        InlineKeyboardButton.builder()
                            .text("Confirm")
                            .callbackData("confirm")
                            .build()
                    )
                )
                .build()

            return CallbackMessage(sendMessage, IDENTIFIER)
        }
    }
}
