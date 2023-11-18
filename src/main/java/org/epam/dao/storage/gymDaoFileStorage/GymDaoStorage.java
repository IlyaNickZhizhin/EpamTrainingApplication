package org.epam.dao.storage.gymDaoFileStorage;

import org.epam.dao.Dao;
import org.epam.model.gymModel.Model;
import org.epam.storageInFile.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import java.util.concurrent.atomic.AtomicInteger;

@DependsOn("dataInitializer")
public abstract class GymDaoStorage<M extends Model> implements Dao<M> {

    Storage<M> storage;
    private final AtomicInteger AUTO_ID = new AtomicInteger(0);

    protected String namespace;

    @Autowired
    public GymDaoStorage(Storage<M> storage) {
        this.storage = storage;
    }

    @Override
    public void create(M model) {
        model.setId(AUTO_ID.incrementAndGet());
        save(model);
    }

    @Override
    public void save(M model) {
        storage.getGymModels().get(namespace).put(model.getId(), model);
    }

    @Override
    public abstract void update(int id, M model);


    @Override
    public void delete(int id) {
        storage.getGymModels().get(namespace).remove(id);
    }

    @Override
    public M get(int id) {
        return storage.getGymModels().get(namespace).get(id);
    }

}
