package org.epam.dao;

public interface Dao<M> {

    void create(M m);
    void save(M m);
    void update(int id, M m);
    void delete(int id);
    M get(int id);
}