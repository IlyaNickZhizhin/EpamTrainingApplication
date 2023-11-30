package org.epam.dao;

import org.epam.Reader;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerDaoImplImplTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);
    @Mock
    private UserDao userDao = mock(UserDao.class);
    @InjectMocks
    private TrainerDaoImpl trainerDaoImpl;

    private Trainer trainer1;
    private Reader reader;

    @BeforeEach
    public void setup() {
        reader = new Reader();
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testUpdate() {
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        TrainingType trainingType1 = reader.readEntity("trainingTypes/trainingType1", TrainingType.class);
        Trainer trainer = trainer1;
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(trainer1.getId());
        updatedTrainer.setSpecialization(trainer1.getSpecialization());
        updatedTrainer.setUser(trainer1.getUser());
        when(session.get(Trainer.class, 1)).thenReturn(trainer);
        when(session.merge(trainer)).thenReturn(trainer);
        trainerDaoImpl.update(1, updatedTrainer);
        assertEquals(trainer.getSpecialization(), updatedTrainer.getSpecialization());
        verify(session).merge(trainer);
    }

    @Test
    public void testCreate() {
        Trainer trainer = new Trainer();
        doNothing().when(session).persist(trainer);
        trainerDaoImpl.create(trainer);
        verify(session).persist(trainer);
    }

    @Test
    public void testSave() {
        Trainer trainer = new Trainer();
        doNothing().when(session).persist(trainer);
        trainerDaoImpl.save(trainer);
        verify(session).persist(trainer);
    }

    @Test
    public void testGet() {
        int id = 1;
        Trainer trainer = new Trainer();
        when(session.get(Trainer.class, id)).thenReturn(trainer);
        assertEquals(trainer, trainerDaoImpl.get(id));
    }

    @Test
    public void testGetByUserId() {
        User user = reader.readEntity("users/user1", User.class);
        int userId = user.getId();
        when(userDao.get(userId)).thenReturn(user);
        Query<Trainer> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        Trainer trainer = reader.readEntity("trainers/trainer1", Trainer.class);
        when(query.getSingleResultOrNull()).thenReturn(trainer);
        assertEquals(trainer, trainerDaoImpl.getModelByUserId(userId));
    }

    @Test
    public void testGetAll() {
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        Trainer trainer2 = reader.readEntity("trainers/trainer2", Trainer.class);
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        Query<Trainer> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.list()).thenReturn(trainers);
        assertEquals(trainers, trainerDaoImpl.getAll());
    }
}
