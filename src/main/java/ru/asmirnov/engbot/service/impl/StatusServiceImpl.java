package ru.asmirnov.engbot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.Command;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;
import ru.asmirnov.engbot.service.AnswerService;
import ru.asmirnov.engbot.service.CommandService;
import ru.asmirnov.engbot.service.PersonService;
import ru.asmirnov.engbot.service.StatusService;

import java.util.ResourceBundle;

/**
 * @author Alexey Smirnov at 03/07/2018
 */
@Service
public class StatusServiceImpl implements StatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusServiceImpl.class);

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("answerMessages");

    private final DictionaryItemRepository dictionaryItemRepository;
    private final PersonService personService;
    private final AnswerService answerService;
    private final CommandService commandService;

    @Autowired
    public StatusServiceImpl(DictionaryItemRepository dictionaryItemRepository, PersonService personService,
                             AnswerService answerService, CommandService commandService) {
        this.dictionaryItemRepository = dictionaryItemRepository;
        this.personService = personService;
        this.answerService = answerService;
        this.commandService = commandService;
    }

    @Override
    public SendMessage processStatus(Person person, Message message) {
        if (person.getStatus() == PersonStatus.BLOCKED) {
            return answerService.getBlockedAnswer(message);
        }

        if (person.getStatus() == PersonStatus.ACTIVE) {
            if (message.isCommand()) {
                try {
                    Command command = Command.recognize(message.getText());
                    return commandService.processCommand(command, person, message);
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Command {} has not recognized", message.getText());
                }
            }
            return answerService.getChoiceAnswer(message);
        }

        if (PersonStatus.ADD_ENG == person.getStatus()) {
            // save english
            DictionaryItem dictionaryItem = DictionaryItem.DictionaryItemBuilder
                    .aDictionaryItem()
                    .original(message.getText())
                    .userId(person.getId())
                    .build();
            personService.saveOriginalDictionaryItem(person, dictionaryItem);
            return new SendMessage(message.getChatId(), resourceBundle.getString("answers.enter.russian"));
        } else if (PersonStatus.ADD_RUS == person.getStatus()) {
            // save translate
            if (person.getCurrentDictionaryItemId() == null) {
                throw new RuntimeException("Current Dictionary item id is null");
            }

            DictionaryItem dictionaryItem = dictionaryItemRepository.findById(person.getCurrentDictionaryItemId());
            personService.saveDictionaryItemTranslate(person, dictionaryItem, message.getText());

            return answerService.getSavedAnswer(message);
        } else if (PersonStatus.ONLINE == person.getStatus()) {

        }
        return null;
    }
}
