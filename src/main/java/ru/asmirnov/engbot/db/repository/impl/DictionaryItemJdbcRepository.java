package ru.asmirnov.engbot.db.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.incrementer.PostgresSequenceMaxValueIncrementer;
import org.springframework.stereotype.Repository;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.DictionaryItemMark;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;

import java.util.List;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Repository
public class DictionaryItemJdbcRepository implements DictionaryItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PostgresSequenceMaxValueIncrementer sequence;
    private final RowMapper<DictionaryItem> dictionaryRowMapper = (rs, rowNum) -> DictionaryItem.DictionaryItemBuilder
            .aDictionaryItem()
            .id(rs.getLong(1))
            .original(rs.getString(2))
            .translate(rs.getString(3))
            .userId(rs.getLong(4))
            .mark(rs.getString(5) != null ? DictionaryItemMark.valueOf(rs.getString(5)) : null)
            .build();

    public DictionaryItemJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sequence = new PostgresSequenceMaxValueIncrementer(jdbcTemplate.getDataSource(),
                "dictionary_seq");
    }

    @Override
    public DictionaryItem save(final DictionaryItem model) {
        final String q;
        if (model.getId() == null) {
            model.setId(sequence.nextLongValue());
            q = "insert into dictionary (original, translate, person_id, mark, id) VALUES (?, ?, ?, ?, ?)";
        } else {
            q = "update dictionary set original = ?, translate = ?, person_id = ?, mark = ? where id = ?";
        }
        jdbcTemplate.update(q, model.getOriginal(),
                model.getTranslate(),
                model.getUserId(),
                model.getMark(),
                model.getId());
        return model;
    }

    @Override
    public DictionaryItem findById(Long id) {
        return jdbcTemplate.queryForObject("select id, original, translate, person_id, mark from dictionary where id = ?",
                dictionaryRowMapper, id);
    }

    @Override
    public List<DictionaryItem> findAll() {
        return jdbcTemplate.query("select id, original, translate, person_id, mark from dictionary",
                dictionaryRowMapper);
    }

    @Override
    public List<DictionaryItem> findByPersonId(Long personId) {
        return jdbcTemplate.query("select id, original, translate, person_id, mark from dictionary where person_id = ?",
                dictionaryRowMapper, personId);
    }
}
