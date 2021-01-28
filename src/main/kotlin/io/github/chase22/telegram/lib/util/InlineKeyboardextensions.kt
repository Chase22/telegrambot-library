package io.github.chase22.telegram.lib.util

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

fun InlineKeyboardMarkup.InlineKeyboardMarkupBuilder.keyboardRow(vararg buttons: InlineKeyboardButton): InlineKeyboardMarkup.InlineKeyboardMarkupBuilder {
    this.keyboardRow(buttons.toList())
    return this
}

fun inlineTextButton(text: String): InlineKeyboardButton = InlineKeyboardButton.builder()
        .text(text)
        .callbackData(text)
        .build()
