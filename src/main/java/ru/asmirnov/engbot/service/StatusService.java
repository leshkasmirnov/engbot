package ru.asmirnov.engbot.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.Person;

/**
 * @author Alexey Smirnov at 01/07/2018
 */
public interface StatusService {

    SendMessage processStatus(Person person, Message message);
}
