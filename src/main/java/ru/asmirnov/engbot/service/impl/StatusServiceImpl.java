package ru.asmirnov.engbot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.enums.*;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;
import ru.asmirnov.engbot.service.CommandService;
import ru.asmirnov.engbot.service.PersonService;
import ru.asmirnov.engbot.service.StatusService;
import ru.asmirnov.engbot.service.TrainingService;

import java.util.Collections;
import java.util.Random;
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
    private final CommandService commandService;
    private final TrainingService trainingService;
    private final PersonRepository personRepository;
    private Random random = new Random();

    @Autowired
    public StatusServiceImpl(DictionaryItemRepository dictionaryItemRepository, PersonService personService,
                             CommandService commandService, TrainingService trainingService, PersonRepository personRepository) {
        this.dictionaryItemRepository = dictionaryItemRepository;
        this.personService = personService;
        this.commandService = commandService;
        this.trainingService = trainingService;
        this.personRepository = personRepository;
    }

    @Override
    public SendMessage processStatus(Person person, Message message) {
        if (person.getStatus() == PersonStatus.BLOCKED) {
            return new SendMessage(message.getChatId(), resourceBundle.getString("answers.blocked"));
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
            return getChoiceAnswer(message);
        }

        if (PersonStatus.ADD_ENG == person.getStatus()) {
            // save english
            DictionaryItem dictionaryItem = DictionaryItem.DictionaryItemBuilder
                    .aDictionaryItem()
                    .original(message.getText())
                    .userId(person.getId())
                    .status(DictionaryItemStatus.READY)
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

            return new SendMessage(message.getChatId(), resourceBundle.getString("answers.saved"));
        } else if (PersonStatus.ONLINE == person.getStatus()) {
            if (person.getCurrentDictionaryItemId() == null) {
                return trainingService.getNextDictionaryItem(person, message);
            }
            trainingService.checkAnswer(person, message);
            return trainingService.getNextDictionaryItem(person, message);

        }
        throw new UnsupportedOperationException("Unknown status: " + person.getStatus());
    }

    private SendMessage getChoiceAnswer(Message message) {
        SendMessage answer = new SendMessage(message.getChatId(), resourceBundle.getString("answers.default"));

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
}
