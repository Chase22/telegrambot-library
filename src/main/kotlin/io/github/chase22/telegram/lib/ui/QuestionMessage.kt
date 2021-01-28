package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import io.github.chase22.telegram.lib.util.keyboardRow
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

class QuestionMessage(
    private val sendMessage: SendMessage,
    answers: List<QuestionMessageAnswers>,
    private val layout: QuestionMessageLayout = QuestionMessageLayout.HORIZONTAL
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

        val builder = InlineKeyboardMarkup.builder()
        sendMessage.replyMarkup = when(layout) {
            QuestionMessageLayout.HORIZONTAL -> builder.keyboardRow(buttons).build()
            QuestionMessageLayout.VERTICAL -> {
                buttons.forEach {
                    builder.keyboardRow(it)
                }
                builder.build()
            }

        }
        return sendMessage
    }
}

data class QuestionMessageAnswers(
    val answer: String,
    val callback: (absSender: AbsSender, callbackQuery: CallbackQuery) -> Unit,
    val key: String = answer.replace(' ', '-').toLowerCase()
)

enum class QuestionMessageLayout {
    VERTICAL, HORIZONTAL
}