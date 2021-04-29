package io.github.chase22.telegram.lib.callback

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

data class CallbackMessage(
    val message: SendMessage,
    val handlerId: String,
    val attributes: Map<String, String>? = null
)