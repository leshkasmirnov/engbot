package ru.asmirnov.engbot.service;

import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;

public interface PersonService {

    void saveOriginalDictionaryItem(Person person, DictionaryItem dictionaryItem);

    void saveDictionaryItemTranslate(Person person, DictionaryItem dictionaryItem, String translate);
}
