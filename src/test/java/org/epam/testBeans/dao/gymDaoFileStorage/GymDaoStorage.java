package org.epam.testBeans.dao.gymDaoFileStorage;

import lombok.Setter;
import org.epam.dao.Dao;
import org.epam.model.gymModel.Model;
import org.epam.testBeans.storageInFile.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import java.util.concurrent.atomic.AtomicInteger;

@DependsOn("dataInitializer")
@Setter
public abstract class GymDaoStorage<M extends Model> implements Dao<M> {

    @Autowired
    Storage<M> storage;

    protected String namespace;

    @Override
    public M create(M model) {
        model.setId(model.getId());
        save(model);
        return model;
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
