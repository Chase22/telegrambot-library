package io.github.chase22.telegram.lib

import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.bots.AbsSender
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupAdminService @Inject constructor(
    private val absSender: AbsSender
) {
    fun isAdmin(chatId: Long, userId: Int): Boolean {
        val status = getAdminStatus(chatId, userId)
        return status == "creator" || status == "administrator"
    }

    fun isCreator(chatId: Long, userId: Int): Boolean {
        val status = getAdminStatus(chatId, userId)
        return status == "creator"
    }

    fun getAdminStatus(chatId: Long, userId: Int): String =
        absSender.execute(GetChatMember(chatId.toString(), userId))
            .status
            .toLowerCase()
}
