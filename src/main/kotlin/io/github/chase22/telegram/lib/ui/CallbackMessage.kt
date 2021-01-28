package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import javax.inject.Inject
import javax.inject.Singleton

interface CallbackMessage {
    /**
     * @return If the message should stay active after this update
     */
    fun processCallback(absSender: AbsSender, callbackQuery: CallbackQuery, adminService: GroupAdminService): Boolean

    fun getSendMessage(): SendMessage
}

@Singleton
class CallbackMessageService @Inject constructor(
        private val absSender: AbsSender,
        private val adminService: GroupAdminService
) {
    private val activeCallbackMessages: MutableMap<Pair<Long, Int>, CallbackMessage> = HashMap()

    fun processCallback(callbackQuery: CallbackQuery) {
        val key = getKeyFromMessage(callbackQuery.message)

        activeCallbackMessages[key]?.let {
            if (!it.processCallback(absSender, callbackQuery, adminService)) {
                activeCallbackMessages.remove(key)
            }
        }
    }

    fun sendCallbackMessage(callbackMessage: CallbackMessage) {
        val message = absSender.execute(callbackMessage.getSendMessage())

        activeCallbackMessages[getKeyFromMessage(message)] = callbackMessage
    }

    private fun getKeyFromMessage(message: Message): Pair<Long, Int> = Pair(message.chatId, message.messageId)
}
