package io.github.chase22.telegram.lib.callback

import io.micronaut.context.annotation.DefaultImplementation
import javax.inject.Singleton

@DefaultImplementation(MemoryCallbackServiceStorage::class)
interface CallbackServiceStorage {
    fun get(chatId: Long, messageId: Int): CallbackMessage?
    fun save(chatId: Long, messageId: Int, message: CallbackMessage)
    fun remove(chatId: Long, messageId: Int)
}

@Singleton
class MemoryCallbackServiceStorage : CallbackServiceStorage {
    private val activeCallbackMessages: MutableMap<Pair<Long, Int>, CallbackMessage> = HashMap()

    override fun get(chatId: Long, messageId: Int): CallbackMessage? = activeCallbackMessages[Pair(chatId, messageId)]

    override fun save(chatId: Long, messageId: Int, message: CallbackMessage) {
        activeCallbackMessages[Pair(chatId, messageId)] = message
    }

    override fun remove(chatId: Long, messageId: Int) {
        activeCallbackMessages.remove(Pair(chatId, messageId))
    }
}