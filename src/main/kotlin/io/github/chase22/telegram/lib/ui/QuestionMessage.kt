package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import io.github.chase22.telegram.lib.ui.CallbackMessage
import io.github.chase22.telegram.lib.util.keyboardRow
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

class QuestionMessage(
        private val sendMessage: SendMessage,
        private val answerOne: String,
        private val answerTwo: String,
        private val answerOneCallback: (absSender: AbsSender, callbackQuery: CallbackQuery) -> Unit,
        private val answerTwoCallback: (absSender: AbsSender, callbackQuery: CallbackQuery) -> Unit
) : CallbackMessage {
    override fun processCallback(absSender: AbsSender, callbackQuery: CallbackQuery, adminService: GroupAdminService): Boolean {
        if (callbackQuery.data == "answerOne") answerOneCallback(absSender, callbackQuery)
        if (callbackQuery.data == "answerTwo") answerTwoCallback(absSender, callbackQuery)
        return false
    }

    override fun getSendMessage(): SendMessage {
        sendMessage.replyMarkup = InlineKeyboardMarkup.builder().keyboardRow(
                InlineKeyboardButton.builder().text(answerOne).callbackData("answerOne").build(),
                InlineKeyboardButton.builder().text(answerTwo).callbackData("answerTwo").build()
        ).build()
        return sendMessage
    }
}
