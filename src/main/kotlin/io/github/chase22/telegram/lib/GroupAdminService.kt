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
        val status = absSender.execute(GetChatMember.builder()
                .userId(userId)
                .chatId(chatId.toString())
                .build()
        ).status

        return status.toLowerCase() == "creator" || status.toLowerCase() == "administrator"
    }
}
