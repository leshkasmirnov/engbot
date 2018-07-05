package ru.asmirnov.engbot.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.Person;

/**
 * @author Alexey Smirnov at 04/07/2018
 */
public interface TrainingService {

    SendMessage getNextDictionaryItem(Person person, Message message);

    void checkAnswer(Person person, Message message);
}
