package org.epam.dao;

/**
 * This interface is used to implement the DAO pattern.
 * @param <M>
 * @see GymAbstractDao
 * @see TraineeDaoImpl
 * @see TrainerDaoImpl
 * @see TrainingDaoImpl
 */
public interface Dao<M> {

    M create(M m);
    void save(M m);
    M update(int id, M m);
    void delete(int id);
    M get(int id);
}