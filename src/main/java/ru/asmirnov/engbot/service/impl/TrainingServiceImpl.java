package ru.asmirnov.engbot.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.enums.DictionaryItemMark;
import ru.asmirnov.engbot.enums.DictionaryItemStatus;
import ru.asmirnov.engbot.enums.DictionaryItemType;
import ru.asmirnov.engbot.service.PersonService;
import ru.asmirnov.engbot.service.TrainingService;

import java.text.MessageFormat;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @author Alexey Smirnov at 04/07/2018
 */
@Service
public class TrainingServiceImpl implements TrainingService {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("answerMessages");
    private Random random = new Random();

    private final DictionaryItemRepository dictionaryItemRepository;
    private final PersonRepository personRepository;
    private final PersonService personService;

    public TrainingServiceImpl(DictionaryItemRepository dictionaryItemRepository, PersonRepository personRepository, PersonService personService) {
        this.dictionaryItemRepository = dictionaryItemRepository;
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @Override
    public SendMessage getNextDictionaryItem(Person person, Message message) {
        DictionaryItem dictionaryItem = dictionaryItemRepository.findRandom(person.getId(), DictionaryItemStatus.READY);
        if (dictionaryItem == null) {
            personService.finishTraining(person);
            return new SendMessage(message.getChatId(), resourceBundle.getString("answers.training.finished"));
        }

        person.setCurrentDictionaryItemId(dictionaryItem.getId());

        String answerMsg;
        if (random.nextBoolean()) {
            person.setCurrentDictionaryItemType(DictionaryItemType.ORIGINAL);
            answerMsg = MessageFormat.format(resourceBundle.getString("answers.training.request.russian"),
                    dictionaryItem.getOriginal());
        } else {
            person.setCurrentDictionaryItemType(DictionaryItemType.TRANSLATE);
            answerMsg = MessageFormat.format(resourceBundle.getString("answers.training.request.english"),
                    dictionaryItem.getTranslate());
        }

        personRepository.save(person);
        return new SendMessage(message.getChatId(), answerMsg);
    }

    @Override
    @Transactional
    public void checkAnswer(Person person, Message message) {
        DictionaryItem dictionaryItem = dictionaryItemRepository.findById(person.getCurrentDictionaryItemId());
        String userAnswer = message.getText();

        if (DictionaryItemType.ORIGINAL == person.getCurrentDictionaryItemType()) {
            dictionaryItem.setMark(userAnswer.equalsIgnoreCase(dictionaryItem.getTranslate()) ? DictionaryItemMark.OK : DictionaryItemMark.WRONG);
        } else if (DictionaryItemType.TRANSLATE == person.getCurrentDictionaryItemType()) {
            dictionaryItem.setMark(userAnswer.equalsIgnoreCase(dictionaryItem.getOriginal()) ? DictionaryItemMark.OK : DictionaryItemMark.WRONG);
        }
        dictionaryItem.setStatus(DictionaryItemStatus.PASSED);
        dictionaryItemRepository.save(dictionaryItem);

        person.setCurrentDictionaryItemId(null);
        person.setCurrentDictionaryItemType(null);
        personRepository.save(person);
    }
}
