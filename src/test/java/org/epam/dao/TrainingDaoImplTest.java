package org.epam.dao;

import org.epam.model.gymModel.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.epam.Supplier.training1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TrainingDaoImplTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);
    @Mock
    private UserDaoImpl userDao = mock(UserDaoImpl.class);
    private TrainingDaoImpl trainingDao = new TrainingDaoImpl(sessionFactory, userDao);
    @BeforeEach
    public void setup() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testUpdate() {
        Training training = training1;
        Training updatedTraining = new Training();
        updatedTraining.setId(training.getId());
        updatedTraining.setTrainingDate(LocalDate.now());
        when(session.get(Training.class, 1)).thenReturn(training);
        trainingDao.update(1, updatedTraining);
        assertEquals(training.getTrainingDate(), updatedTraining.getTrainingDate());
        verify(session).merge(training);
    }

    @Test
    public void testCreate() {
        doNothing().when(session).persist(training1);
        trainingDao.create(training1);
        verify(session).persist(training1);
    }

    @Test
    public void testSave() {
        doNothing().when(session).persist(training1);
        trainingDao.save(training1);
        verify(session).persist(training1);
    }

    @Test
    public void testGet() {
        when(session.get(Training.class, training1.getId())).thenReturn(training1);
        assertEquals(training1, trainingDao.get(training1.getId()));
    }

    @Test
    public void testGetAll() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        trainings.add(new Training());
        Query query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.list()).thenReturn(trainings);
        assertEquals(trainings, trainingDao.getAll());
    }
}
