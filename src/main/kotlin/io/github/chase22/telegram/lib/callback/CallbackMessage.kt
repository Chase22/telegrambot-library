package io.github.chase22.telegram.lib.callback

import io.github.chase22.telegram.lib.GroupAdminService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender

interface CallbackMessage {
    /**
     * @return If the message should stay active after this update
     */
    fun processCallback(absSender: AbsSender, callbackQuery: CallbackQuery, adminService: GroupAdminService): Boolean

    fun getSendMessage(): SendMessage
}