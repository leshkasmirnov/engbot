package ru.asmirnov.engbot.db.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.incrementer.PostgresSequenceMaxValueIncrementer;
import org.springframework.stereotype.Repository;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;
import ru.asmirnov.engbot.db.repository.PersonRepository;

import java.util.List;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PostgresSequenceMaxValueIncrementer sequence;
    private final RowMapper<Person> personRowMapper = (rs, rowNum) -> Person.PersonBuilder.aPerson()
            .id(rs.getLong(1))
            .extId(rs.getLong(2))
            .status(PersonStatus.valueOf(rs.getString(3)))
            .build();

    public PersonRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sequence = new PostgresSequenceMaxValueIncrementer(jdbcTemplate.getDataSource(),
                "person_seq");
    }

    @Override
    public Person save(Person person) {
        final String q;
        if (person.getId() == null) {
            person.setId(sequence.nextLongValue());
            q = "insert into person (ext_id, status, id) VALUES (?,?,?)";
        } else {
            q = "update person set ext_id = ?, status = ? where id = ?";
        }
        jdbcTemplate.update(q, person.getExtId(), person.getStatus().toString(), person.getId());
        return person;
    }

    @Override
    public Person findById(Long id) {
        return jdbcTemplate.queryForObject("select id, ext_id, status from person where id = ?",
                personRowMapper);
    }

    @Override
    public List<Person> findAll() {
        return jdbcTemplate.query("select id, ext_id, status from person",
                personRowMapper);
    }

    @Override
    public Person findByExtId(Long extId) {
        try {
            return jdbcTemplate.queryForObject("select id, ext_id, status from person where ext_id = ?",
                    personRowMapper, extId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
