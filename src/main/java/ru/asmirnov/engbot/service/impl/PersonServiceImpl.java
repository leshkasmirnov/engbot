package ru.asmirnov.engbot.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.enums.PersonStatus;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.service.PersonService;

/**
 * @author Alexey Smirnov at 01/07/2018
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final DictionaryItemRepository dictionaryItemRepository;

    public PersonServiceImpl(PersonRepository personRepository, DictionaryItemRepository dictionaryItemRepository) {
        this.personRepository = personRepository;
        this.dictionaryItemRepository = dictionaryItemRepository;
    }

    @Override
    @Transactional
    public void saveOriginalDictionaryItem(Person person, DictionaryItem dictionaryItem) {
        DictionaryItem saved = dictionaryItemRepository.save(dictionaryItem);

        person.setCurrentDictionaryItemId(saved.getId());
        person.setStatus(PersonStatus.ADD_RUS);
        personRepository.save(person);
    }

    @Override
    @Transactional
    public void saveDictionaryItemTranslate(Person person, DictionaryItem dictionaryItem, String translate) {
        dictionaryItem.setTranslate(translate);
        dictionaryItemRepository.save(dictionaryItem);

        person.setCurrentDictionaryItemId(null);
        person.setStatus(PersonStatus.ACTIVE);
        personRepository.save(person);
    }
}
