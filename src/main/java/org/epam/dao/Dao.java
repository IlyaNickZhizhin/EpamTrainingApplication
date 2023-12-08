package org.epam.dao;

import java.util.Optional;

/**
 * This interface is used to implement the DAO pattern.
 * @param <M>
 * @see GymAbstractDao
 * @see TraineeDaoImpl
 * @see TrainerDaoImpl
 * @see TrainingDaoImpl
 */
public interface Dao<M> {

    Optional<M> create(M m);
    void save(M m);
    Optional<M> update(int id, M m);
    Optional<M> delete(int id);
    Optional<M> get(int id);
}