package ru.asmirnov.engbot.service.impl;

import org.springframework.stereotype.Service;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.service.PersonService;

/**
 * @author Alexey Smirnov at 01/07/2018
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void setStatus(Person person, PersonStatus personStatus, Boolean requested) {
        person.setStatus(personStatus);
        person.setStatusRequested(requested);
        personRepository.save(person);
    }
}
