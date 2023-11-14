package org.epam.dao.gymDao;

import org.epam.config.Storage;
import org.epam.dao.Dao;
import org.epam.model.gymModel.Model;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class GymDaoStorage<Id extends Integer, T extends Model> implements Dao<Model> {

    Storage storage;
    private final AtomicInteger AUTO_ID = new AtomicInteger(0);

    protected String namespace;
    Map<String, Map<Integer, Model>> models;

    public GymDaoStorage(Storage storage) {
        this.storage = storage;
        this.models = storage.getGymModels();
    }

    @Override
    public void create(Model model) {
        model.setId(AUTO_ID.incrementAndGet());
        save(model);
    }

    @Override
    public void save(Model model) {
        (models.get(namespace)).put(model.getId(), model);
    }

    @Override
    public abstract void update(int id, Model model);


    @Override
    public void delete(int id) {
        models.get(namespace).remove(id);
    }

    @Override
    public Model get(int id) {
        return models.get(namespace).get(id);
    }

}
