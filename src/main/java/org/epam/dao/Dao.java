package org.epam.dao;

public interface Dao<T> {

    void create(T t);
    void save(T t);
    void update(int id, T t);
    void delete(int id);
    T get(int id);
}