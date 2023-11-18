package org.epam.dao;

import jakarta.transaction.Transactional;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
public abstract class GymAbstractDaoImpl<M extends Model> implements Dao<M>{

    protected Class<M> modelClass;
    @Autowired
    protected SessionFactory sessionFactory;

    public GymAbstractDaoImpl() {
        this.modelClass = (Class<M>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }



    @Autowired
    private UserDaoImpl userDao;

    @Override
    public void create(M model) {
        save(model);
    }

    @Override
    @Transactional
    public void save(M model) {
        sessionFactory.getCurrentSession().persist(model);
    }

    @Override
    public abstract void update(int id, M model);

    @Override
    @Transactional
    public void delete(int id) {
        sessionFactory.getCurrentSession().remove(get(id));
    }

    @Override
    public M get(int id) {
        return sessionFactory.getCurrentSession().get(modelClass, id);
    }

    public M getByUserId(int userId) {
        User user = userDao.get(userId);
        return sessionFactory.getCurrentSession()
                .createQuery("from " + modelClass.getSimpleName() + " where user = :user", modelClass)
                .setParameter("user", user)
                .getSingleResult();
    }
    public List<M> getAll(){
        return sessionFactory.getCurrentSession()
                .createQuery("from " + modelClass.getName(), modelClass).list();
    }
}
