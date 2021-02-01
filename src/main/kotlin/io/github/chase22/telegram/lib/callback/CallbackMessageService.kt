package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import io.github.chase22.telegram.lib.callback.CallbackMessage
import io.github.chase22.telegram.lib.callback.CallbackServiceStorage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallbackMessageService @Inject constructor(
    private val absSender: AbsSender,
    private val adminService: GroupAdminService,
    private val storage: CallbackServiceStorage
) {
    fun processCallback(callbackQuery: CallbackQuery) {
        val message = callbackQuery.message

        storage.get(message.chatId, message.messageId)?.let {
            if (!it.processCallback(absSender, callbackQuery, adminService)) {
                storage.remove(message.chatId, message.messageId)
            }
        }
    }

    fun sendCallbackMessage(callbackMessage: CallbackMessage) {
        val message = absSender.execute(callbackMessage.getSendMessage())

        storage.save(message.chatId, message.messageId, callbackMessage)
    }
}

