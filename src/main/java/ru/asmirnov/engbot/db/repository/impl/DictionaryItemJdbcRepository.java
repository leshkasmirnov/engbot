package ru.asmirnov.engbot.db.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.asmirnov.engbot.db.domain.DictionaryItem;
import ru.asmirnov.engbot.db.domain.DictionaryItemMark;
import ru.asmirnov.engbot.db.repository.DictionaryItemRepository;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author Alexey Smirnov at 15/04/2018
 */
@Repository
public class DictionaryItemJdbcRepository implements DictionaryItemRepository {

    private final JdbcTemplate jdbcTemplate;
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
    }

    @Override
    public DictionaryItem save(final DictionaryItem model) {
        final String q;
        boolean insert = model.getId() == null;

        if (insert) {
            q = "insert into dictionary (original, translate, person_id, mark) VALUES (?, ?, ?, ?)";
        } else {
            q = "update dictionary set original = ?, translate = ?, person_id = ?, mark = ? where id = ?";
        }

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(q, new String[]{"id"});

            ps.setString(1, model.getOriginal());
            ps.setString(2, model.getTranslate());
            ps.setObject(3, model.getUserId());
            ps.setString(4, model.getMark() != null ? model.getMark().name() : null);
            if (!insert) {
                ps.setLong(5, model.getId());
            }
            return ps;
        }, generatedKeyHolder);

        model.setId((Long) generatedKeyHolder.getKey());
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
