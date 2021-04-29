package io.github.chase22.telegram.lib.callback

import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CallbackMessageService @Inject constructor(
    private val absSender: AbsSender,
    private val storage: CallbackServiceStorage,
    initialMessageHandlers: List<CallbackMessageHandler>
) {
    private val messageHandlers: Map<String, CallbackMessageHandler> =
        initialMessageHandlers.associateBy { it.identifier }

    fun processCallback(callbackQuery: CallbackQuery) {
        val message = callbackQuery.message

        storage.get(message.chatId, message.messageId)?.let {
            messageHandlers[it.handlerId]?.let { handler ->
                if (!handler.processUpdate(callbackQuery)) {
                    storage.remove(message.chatId, message.messageId)
                }
            }
        }
    }

    fun sendCallbackMessage(callbackMessage: CallbackMessage) {
        val message = absSender.execute(callbackMessage.message)

        storage.save(
            message.chatId,
            message.messageId,
            callbackMessage.handlerId,
            callbackMessage.attributes ?: emptyMap()
        )
    }
}

