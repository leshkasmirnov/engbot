package ru.asmirnov.engbot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;
import ru.asmirnov.engbot.service.AnswerService;
import ru.asmirnov.engbot.service.PersonService;
import ru.asmirnov.engbot.service.StatusService;

import java.util.ResourceBundle;

/**
 * @author Alexey Smirnov at 03/07/2018
 */
@Service
public class StatusServiceImpl implements StatusService {

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("answerMessages");

    private final DictionaryItemRepository dictionaryItemRepository;
    private final PersonService personService;
    private final AnswerService answerService;
    private StatusService statusService;

    @Autowired
    public StatusServiceImpl(DictionaryItemRepository dictionaryItemRepository, PersonService personService,
                             AnswerService answerService) {
        this.dictionaryItemRepository = dictionaryItemRepository;
        this.personService = personService;
        this.answerService = answerService;
    }

    @Override
    @Transactional
    public SendMessage processStatus(Person person, Message message) {
        if (PersonStatus.ADD_ENG == person.getStatus()) {
            if (Boolean.TRUE.equals(person.getStatusRequested())) {
                return new SendMessage(message.getChatId(), resourceBundle.getString("answers.enter.english"));
            }
            // save english
            DictionaryItem dictionaryItem = DictionaryItem.DictionaryItemBuilder
                    .aDictionaryItem()
                    .original(message.getText())
                    .userId(person.getId())
                    .build();
            dictionaryItem = dictionaryItemRepository.save(dictionaryItem);

            person.setCurrentDictionaryItemId(dictionaryItem.getId());
            personService.setStatus(person, PersonStatus.ADD_RUS, Boolean.TRUE);
            return statusService.processStatus(person, message);
        } else if (PersonStatus.ADD_RUS == person.getStatus()) {
            if (Boolean.TRUE.equals(person.getStatusRequested())) {
                return new SendMessage(message.getChatId(), resourceBundle.getString("answers.enter.russian"));
            }

            // save translate
            if (person.getCurrentDictionaryItemId() == null) {
                throw new RuntimeException("Current Dictionary item id is null");
            }
            DictionaryItem dictionaryItem = dictionaryItemRepository.findById(person.getCurrentDictionaryItemId());
            dictionaryItem.setTranslate(message.getText());
            dictionaryItemRepository.save(dictionaryItem);

            person.setCurrentDictionaryItemId(null);
            personService.setStatus(person, PersonStatus.ACTIVE, Boolean.FALSE);
            return answerService.getSavedAnswer(message);
        } else if (PersonStatus.ONLINE == person.getStatus()) {

        }
        return null;
    }

    @Autowired
    public void setStatusService(StatusService statusService) {
        this.statusService = statusService;
    }
}
