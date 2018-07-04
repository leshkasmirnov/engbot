package ru.asmirnov.engbot.service;

import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;

public interface PersonService {

    void setStatus(Person person, PersonStatus personStatus, Boolean requested);
}
