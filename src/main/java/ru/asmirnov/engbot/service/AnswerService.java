package ru.asmirnov.engbot.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

/**
 * @author Alexey Smirnov at 01/07/2018
 */
public interface AnswerService {

    SendMessage getBlockedAnswer(Message message);

    SendMessage getDefaultAnswer(Message message);
}
