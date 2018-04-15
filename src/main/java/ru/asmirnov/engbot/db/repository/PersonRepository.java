package ru.asmirnov.engbot.db.repository;

import ru.asmirnov.engbot.db.domain.Person;

public interface PersonRepository extends CrudRepository<Person> {

    Person findByExtId(Long extId);
}
