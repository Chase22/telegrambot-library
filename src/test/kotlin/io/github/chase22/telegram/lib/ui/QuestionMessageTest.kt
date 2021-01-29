package io.github.chase22.telegram.lib.ui

import io.github.chase22.telegram.lib.GroupAdminService
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verifyAll
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.bots.AbsSender

val absSenderMock = mockk<AbsSender>()
val adminServiceMock = mockk<GroupAdminService>()

val sendMessage = SendMessage()

var answerOneCalls: Int = 0
var answerTwoCalls: Int = 0
val answerOneCallbackQuery = CallbackQuery().apply {
    data = "answer1"
}

val answers = listOf(
    QuestionMessageAnswer("answer1", { _, _ -> answerOneCalls++ }),
    QuestionMessageAnswer("answer2", { _, _ -> answerTwoCalls++ }),
)

fun answerToButton(answer: QuestionMessageAnswer): InlineKeyboardButton = InlineKeyboardButton
    .builder()
    .callbackData(answer.key)
    .text(answer.answer)
    .build()


class QuestionMessageTest : StringSpec({
    beforeEach {
        answerOneCalls = 0
        answerTwoCalls = 0
    }

    "should set the buttons correctly in the default layout" {
        val sut = QuestionMessage(sendMessage, answers)
        (sut.getSendMessage().replyMarkup as InlineKeyboardMarkup).keyboard shouldBe
                listOf(answers.map { answerToButton(it) })
    }
    "should set the buttons correctly in the vertical layout" {
        val sut = QuestionMessage(sendMessage, answers, QuestionMessageLayout.VERTICAL)
        (sut.getSendMessage().replyMarkup as InlineKeyboardMarkup).keyboard shouldBe
                answers.map { listOf(answerToButton(it)) }
    }

    "should call the correct callback when the first button is pressed" {
        val sut = QuestionMessage(sendMessage, answers)
        sut.processCallback(absSenderMock, answerOneCallbackQuery, adminServiceMock)
        answerOneCalls shouldBe 1
        answerTwoCalls shouldBe 0
    }
})

class QuestionMessageAnswerTest : StringSpec({
    "should generate a key from the answer if none is given" {
        forAll(
            row("someTest", "sometest"),
            row("some1test", "some1test"),
            row("some test with spaces", "some-test-with-spaces"),
            row("SoMe OThEr tesT WitH Spaces", "some-other-test-with-spaces")
        ) { answer, expectedKey ->
            QuestionMessageAnswer(answer, { _, _ -> }).key shouldBe expectedKey
        }
    }
})