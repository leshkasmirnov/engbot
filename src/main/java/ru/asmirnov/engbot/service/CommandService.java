package ru.asmirnov.engbot.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.Command;
import ru.asmirnov.engbot.db.domain.Person;

/**
 * @author Alexey Smirnov at 04/07/2018
 */
public interface CommandService {

    SendMessage processCommand(Command command, Person person, Message message);
}
