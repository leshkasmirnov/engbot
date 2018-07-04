package ru.asmirnov.engbot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.asmirnov.engbot.db.domain.Command;
import ru.asmirnov.engbot.service.AnswerService;

import java.util.Collections;
import java.util.ResourceBundle;

/**
 * @author Alexey Smirnov at 01/07/2018
 */
@Service
public class AnswerServiceImpl implements AnswerService {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("answerMessages");

    @Override
    public SendMessage getBlockedAnswer(Message message) {
        return createSendMessage(message, resourceBundle.getString("answers.blocked"));
    }

    @Override
    public SendMessage getDefaultAnswer(Message message) {
        SendMessage answer = createSendMessage(message, resourceBundle.getString("answers.default"));

        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add(Command.ADD.toString());
        row.add(Command.START.toString());

        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup()
                .setKeyboard(Collections.singletonList(row))
                .setOneTimeKeyboard(Boolean.TRUE);// for hide keyboard after use

        // Add it to the message
        answer.setReplyMarkup(keyboardMarkup);
        return answer;
    }

    private SendMessage createSendMessage(Message message, String text) {
        return new SendMessage(message.getChatId(), text);
    }
}
