package ru.asmirnov.engbot.db.repository;

import ru.asmirnov.engbot.db.domain.DictionaryItem;

import java.util.List;

public interface DictionaryItemRepository extends CrudRepository<DictionaryItem> {

    List<DictionaryItem> findByPersonId(Long personId);
}
