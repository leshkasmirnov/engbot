package ru.asmirnov.engbot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.enums.Command;
import ru.asmirnov.engbot.enums.PersonStatus;
import ru.asmirnov.engbot.service.CommandService;

import java.util.ResourceBundle;

/**
 * @author Alexey Smirnov at 04/07/2018
 */
@Service
public class CommandServiceImpl implements CommandService {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("answerMessages");

    private final PersonRepository personRepository;

    @Autowired
    public CommandServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public SendMessage processCommand(Command command, Person person, Message message) {
        if (Command.ADD == command) {
            person.setStatus(PersonStatus.ADD_ENG);
            personRepository.save(person);
            return new SendMessage(message.getChatId(), resourceBundle.getString("answers.enter.english"));
        } else if (Command.START == command) {
            person.setStatus(PersonStatus.ONLINE);
            personRepository.save(person);
            // start training
            return new SendMessage(message.getChatId(), resourceBundle.getString("answers.start"));
        } else if (Command.STOP == command) {
            // stop training
            person.setStatus(PersonStatus.ACTIVE);
            personRepository.save(person);
        }
        throw new UnsupportedOperationException("Processing command " + command + " haven't realized yet");
    }
}
