package io.github.chase22.telegram.lib.callback

import io.micronaut.context.annotation.DefaultImplementation
import javax.inject.Singleton

data class SavedCallbackMessage(
    val chatId: Long,
    val messageId: Int,
    val handlerId: String,
    val attributes: Map<String, String>
)

@DefaultImplementation(MemoryCallbackServiceStorage::class)
interface CallbackServiceStorage {
    fun get(chatId: Long, messageId: Int): SavedCallbackMessage?
    fun save(chatId: Long, messageId: Int, handlerId: String, attributes: Map<String, String>)
    fun remove(chatId: Long, messageId: Int)
}

@Singleton
class MemoryCallbackServiceStorage : CallbackServiceStorage {
    private val activeCallbackMessages: MutableMap<Pair<Long, Int>, SavedCallbackMessage> = HashMap()

    override fun get(chatId: Long, messageId: Int): SavedCallbackMessage? = activeCallbackMessages[Pair(chatId, messageId)]

    override fun save(chatId: Long, messageId: Int, handlerId: String, attributes: Map<String, String>) {
        activeCallbackMessages[Pair(chatId, messageId)] = SavedCallbackMessage(chatId, messageId, handlerId, attributes)
    }

    override fun remove(chatId: Long, messageId: Int) {
        activeCallbackMessages.remove(Pair(chatId, messageId))
    }
}