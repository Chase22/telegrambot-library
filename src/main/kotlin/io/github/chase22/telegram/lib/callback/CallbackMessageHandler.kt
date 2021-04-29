package io.github.chase22.telegram.lib.callback

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery

interface CallbackMessageHandler {
    val identifier: String

    /**
     * @return true if the message should stay active. False if the message should be removed
     */
    fun processUpdate(callbackQuery: CallbackQuery): Boolean
}