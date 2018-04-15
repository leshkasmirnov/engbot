package ru.asmirnov.engbot.db.repository;

import java.util.List;

public interface CrudRepository <T> {
    T save(T model);
    T findById(Long id);
    List<T> findAll();
}
