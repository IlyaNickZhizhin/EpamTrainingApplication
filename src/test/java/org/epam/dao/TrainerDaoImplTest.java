package org.epam.dao;

import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.epam.TestDatabaseInitializer.trainer1;
import static org.epam.TestDatabaseInitializer.trainer2;
import static org.epam.TestDatabaseInitializer.trainingType1;
import static org.epam.TestDatabaseInitializer.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TrainerDaoImplTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);
    @Mock
    private UserDaoImpl userDao = mock(UserDaoImpl.class);
    @InjectMocks
    private TrainerDaoImpl trainerDao;
    @BeforeEach
    public void setup() {
        initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testUpdate() {
        Trainer trainer = trainer1;
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(trainer.getId());
        updatedTrainer.setSpecialization(trainingType1);
        when(session.get(Trainer.class, 1)).thenReturn(trainer);
        trainerDao.update(1, updatedTrainer);
        assertEquals(trainer.getSpecialization(), updatedTrainer.getSpecialization());
        verify(session).merge(trainer);
    }

    @Test
    public void testCreate() {
        Trainer trainer = new Trainer();
        doNothing().when(session).persist(trainer);
        trainerDao.create(trainer);
        verify(session).persist(trainer);
    }

    @Test
    public void testSave() {
        Trainer trainer = new Trainer();
        doNothing().when(session).persist(trainer);
        trainerDao.save(trainer);
        verify(session).persist(trainer);
    }

    @Test
    public void testGet() {
        int id = 1;
        Trainer trainer = new Trainer();
        when(session.get(Trainer.class, id)).thenReturn(trainer);
        assertEquals(trainer, trainerDao.get(id));
    }

    @Test
    public void testGetByUserId() {
        int userId = user1.getId();
        User user = user1;
        when(userDao.get(userId)).thenReturn(user);
        Query query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        Trainer trainer = new Trainer();
        when(query.getSingleResult()).thenReturn(trainer);
        assertEquals(trainer, trainerDao.getByUserId(userId));
    }

    @Test
    public void testGetAll() {
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer1);
        trainers.add(trainer2);
        Query query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query);
        when(query.list()).thenReturn(trainers);
        assertEquals(trainers, trainerDao.getAll());
    }
}
