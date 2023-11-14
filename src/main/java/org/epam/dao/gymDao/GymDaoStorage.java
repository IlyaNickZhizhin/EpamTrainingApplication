package org.epam.dao.gymDao;

import org.epam.storageInFile.Storage;
import org.epam.dao.Dao;
import org.epam.model.gymModel.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@DependsOn("dataInitializer")
public abstract class GymDaoStorage<M extends Model> implements Dao<Model> {

    Storage<M> storage;
    private final AtomicInteger AUTO_ID = new AtomicInteger(0);

    protected String namespace;
    Map<String, Map<Integer, Model>> models;

    @Autowired
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
