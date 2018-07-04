package ru.asmirnov.engbot.db.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.asmirnov.engbot.db.domain.Person;
import ru.asmirnov.engbot.db.domain.PersonStatus;
import ru.asmirnov.engbot.db.repository.PersonRepository;
import ru.asmirnov.engbot.util.JdbsUtils;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Repository
public class PersonJdbcRepositoryImpl implements PersonRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Person> personRowMapper = (rs, rowNum) -> Person.PersonBuilder.aPerson()
            .id(rs.getLong(1))
            .extId(rs.getLong(2))
            .status(PersonStatus.valueOf(rs.getString(3)))
            .currentDictionaryItemId(JdbsUtils.getLong(rs.getLong(4), rs.wasNull()))
            .build();

    @Autowired
    public PersonJdbcRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Person save(Person person) {
        final String q;
        boolean insert = person.getId() == null;

        if (insert) {
            q = "insert into person (ext_id, status, curr_dict_item_id) VALUES (?,?,?)";
        } else {
            q = "update person set ext_id = ?, status = ?, curr_dict_item_id = ? where id = ?";
        }

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(q, new String[]{"id"});

            ps.setObject(1, person.getExtId());
            ps.setString(2, person.getStatus().name());
            ps.setObject(3, person.getCurrentDictionaryItemId());
            if (!insert) {
                ps.setLong(4, person.getId());
            }
            return ps;
        }, generatedKeyHolder);

        person.setId((Long) generatedKeyHolder.getKey());
        return person;
    }

    @Override
    public Person findById(Long id) {
        return jdbcTemplate.queryForObject("select id, ext_id, status, curr_dict_item_id from person where id = ?",
                personRowMapper);
    }

    @Override
    public List<Person> findAll() {
        return jdbcTemplate.query("select id, ext_id, status, curr_dict_item_id from person",
                personRowMapper);
    }

    @Override
    public Person findByExtId(Long extId) {
        try {
            return jdbcTemplate.queryForObject("select id, ext_id, status, curr_dict_item_id from person where ext_id = ?",
                    personRowMapper, extId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
