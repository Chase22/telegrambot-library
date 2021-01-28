package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

class QuestionMessage(
    private val sendMessage: SendMessage,
    answers: List<QuestionMessageAnswers>
) : CallbackMessage {
    private val answerMap: Map<String, QuestionMessageAnswers> = answers.map { it.key to it }.toMap()

    override fun processCallback(
        absSender: AbsSender,
        callbackQuery: CallbackQuery,
        adminService: GroupAdminService
    ): Boolean {
        answerMap[callbackQuery.data]?.callback?.invoke(absSender, callbackQuery)
        return false
    }

    override fun getSendMessage(): SendMessage {
        val buttons = answerMap.values.map {
            InlineKeyboardButton.builder().text(it.answer).callbackData(it.key).build()
        }

        sendMessage.replyMarkup = InlineKeyboardMarkup.builder().keyboardRow(buttons).build()
        return sendMessage
    }
}

data class QuestionMessageAnswers(
    val answer: String,
    val callback: (absSender: AbsSender, callbackQuery: CallbackQuery) -> Unit,
    val key: String = answer.replace(' ', '-').toLowerCase()
)