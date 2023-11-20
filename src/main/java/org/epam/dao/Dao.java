package org.epam.dao;

/**
 * This interface is used to implement the DAO pattern.
 * @param <M>
 * @see org.epam.dao.GymAbstractDaoImpl
 * @see org.epam.dao.TraineeDaoImpl
 * @see org.epam.dao.TrainerDaoImpl
 * @see org.epam.dao.TrainingDaoImpl
 */
public interface Dao<M> {

    M create(M m);
    void save(M m);
    void update(int id, M m);
    void delete(int id);
    M get(int id);
}