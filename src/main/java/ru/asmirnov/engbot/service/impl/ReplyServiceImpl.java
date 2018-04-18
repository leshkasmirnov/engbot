package ru.asmirnov.engbot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.asmirnov.engbot.db.domain.Command;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.service.ReplyService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Service
@SuppressWarnings("unused")
public class ReplyServiceImpl implements ReplyService {

    private final PersonRepository personRepository;
    private final DictionaryItemRepository dictionaryItemRepository;

    public ReplyServiceImpl(PersonRepository personRepository, DictionaryItemRepository dictionaryItemRepository) {
        this.personRepository = personRepository;
        this.dictionaryItemRepository = dictionaryItemRepository;
    }

    @Override
    public SendMessage reply(Message message) {
        long fromId = message.getFrom().getId().longValue();
        Person person = personRepository.findByExtId(fromId);
        if (person == null) {
            person = personRepository.save(Person.PersonBuilder.aPerson().extId(fromId).status(PersonStatus.ACTIVE).build());
        }

        if (person.getStatus() == PersonStatus.BLOCKED) {
            return getBlockedAnswer(message);
        }

        if (person.getStatus() == PersonStatus.ACTIVE && message.isCommand()) {
            Command command = Command.recognize(message.getText());
            return processCommand(person, message, command);
        }

        return processStatus(person, message);
        //return getDefaultAnswer(message);
    }

    private SendMessage processCommand(Person person, Message message, Command command) {
        if (Command.ADD == command) {
            person.setStatus(PersonStatus.ADDENG);
            personRepository.save(person);
            return new SendMessage(message.getChatId(), "Enter english word:");
        }
        return null;
    }

    private SendMessage processStatus(Person person, Message message) {
        if (person.getStatus() == PersonStatus.ADDENG) {
            // save english
            DictionaryItem dictionaryItem = DictionaryItem.DictionaryItemBuilder
                    .aDictionaryItem()
                    .original(message.getText())
                    .userId(person.getId())
                    .build();
            dictionaryItem = dictionaryItemRepository.save(dictionaryItem);

            person.setCurrentDictionaryItemId(dictionaryItem.getId());
            person.setStatus(PersonStatus.ADDRUS);
            personRepository.save(person);
            return new SendMessage(message.getChatId(), "Enter russian word:");
        }

        if (person.getStatus() == PersonStatus.ADDRUS) {
            // save translate
            if (person.getCurrentDictionaryItemId() == null) {
                throw new RuntimeException("Current Dictionary item id is null");
            }
            DictionaryItem dictionaryItem = dictionaryItemRepository.findById(person.getCurrentDictionaryItemId());
            dictionaryItem.setTranslate(message.getText());
            dictionaryItemRepository.save(dictionaryItem);

            person.setCurrentDictionaryItemId(null);
            person.setStatus(PersonStatus.ACTIVE);
            personRepository.save(person);
            return getDefaultAnswer(message);
        }

        if (person.getStatus() == PersonStatus.ONLINE) {

        }
        return getDefaultAnswer(message);
    }

    private SendMessage getBlockedAnswer(Message message) {
        return new SendMessage(message.getChatId(), "You are blocked. Sorry.");
    }

    private SendMessage getDefaultAnswer(Message message) {
        SendMessage answer = new SendMessage(message.getChatId(), "Select one of the following:");

        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("/add");
        row.add("/start");
        // Add the first row to the keyboard
        keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        answer.setReplyMarkup(keyboardMarkup);
        return answer;
    }

}
