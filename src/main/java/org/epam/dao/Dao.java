package org.epam.dao;

import java.util.Optional;

public interface Dao<M> {

    Optional<M> create(M m);
    void save(M m);
    Optional<M> update(int id, M m);
    Optional<M> delete(int id);
    Optional<M> get(int id);
}