package ru.asmirnov.engbot.db.repository;

import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.enums.DictionaryItemStatus;

import java.util.List;

public interface DictionaryItemRepository extends CrudRepository<DictionaryItem> {

    List<DictionaryItem> findByPersonId(Long personId);

    DictionaryItem findRandom(Long personId, DictionaryItemStatus dictionaryItemStatus);

    void setUnPassedByPerson(Person person);
}
