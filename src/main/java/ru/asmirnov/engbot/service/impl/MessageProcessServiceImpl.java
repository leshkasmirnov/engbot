package ru.asmirnov.engbot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.service.MessageProcessService;
import ru.asmirnov.engbot.service.StatusService;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Service
@SuppressWarnings("unused")
public class MessageProcessServiceImpl implements MessageProcessService {

    private final PersonRepository personRepository;
    private final StatusService statusService;

    @Autowired
    public MessageProcessServiceImpl(PersonRepository personRepository, StatusService statusService) {
        this.personRepository = personRepository;
        this.statusService = statusService;
    }

    @Override
    public SendMessage reply(Message message) {
        long fromId = message.getFrom().getId().longValue();
        Person person = personRepository.findByExtId(fromId);
        if (person == null) {
            person = personRepository.save(
                    Person.PersonBuilder.aPerson()
                            .extId(fromId)
                            .status(PersonStatus.ACTIVE)
                            .statusRequested(Boolean.FALSE)
                            .build()
            );
        }
        return statusService.processStatus(person, message);
    }

}
