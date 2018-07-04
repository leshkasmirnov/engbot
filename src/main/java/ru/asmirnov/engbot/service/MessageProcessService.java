package ru.asmirnov.engbot.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
public interface MessageProcessService {

    SendMessage reply(Message message);
}
