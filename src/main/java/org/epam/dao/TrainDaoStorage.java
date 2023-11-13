package org.epam.dao;

import org.epam.model.Model;
import org.epam.model.Trainee;
import org.epam.model.Trainer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TrainDaoStorage<Integer, T extends Model> implements Dao<Model> {

    private final AtomicInteger AUTO_ID = new AtomicInteger(0);
    
    private Map<java.lang.Integer, Model> models;
    
    @Override
    public void save(Model model) {
        models.put(AUTO_ID.getAndIncrement(), model);
    }

    @Override
    public abstract void update(int id, Model model);


    @Override
    public void delete(int id) {
        models.remove(id);
    }

    @Override
    public Model get(int id) {
        return models.get(id);
    }

}
