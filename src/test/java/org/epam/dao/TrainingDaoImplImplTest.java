package org.epam.dao;

import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.epam.TestDatabaseInitializer.trainee3;
import static org.epam.TestDatabaseInitializer.trainee3_Username;
import static org.epam.TestDatabaseInitializer.trainee4;
import static org.epam.TestDatabaseInitializer.trainer1;
import static org.epam.TestDatabaseInitializer.trainer1_Username;
import static org.epam.TestDatabaseInitializer.trainer2;
import static org.epam.TestDatabaseInitializer.training1;
import static org.epam.TestDatabaseInitializer.training2;
import static org.epam.TestDatabaseInitializer.trainingType1;
import static org.epam.TestDatabaseInitializer.user1;
import static org.epam.TestDatabaseInitializer.user3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class TrainingDaoImplImplTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);
    @Mock
    private UserDaoImpl userDao = mock(UserDaoImpl.class);
    @InjectMocks
    private TrainingDaoImpl trainingDaoImpl;
    @BeforeEach
    public void setup() {
        initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    public void testUpdate() {
        Training training = training1;
        Training updatedTraining = new Training();
        updatedTraining.setId(training.getId());
        updatedTraining.setTrainingDate(LocalDate.now());
        when(session.get(Training.class, 1)).thenReturn(training);
        trainingDaoImpl.update(1, updatedTraining);
        assertEquals(training.getTrainingDate(), updatedTraining.getTrainingDate());
        verify(session).merge(training);
    }

    @Test
    public void testCreate() {
        doNothing().when(session).persist(training1);
        trainingDaoImpl.create(training1);
        verify(session).persist(training1);
    }

    @Test
    public void testSave() {
        doNothing().when(session).persist(training1);
        trainingDaoImpl.save(training1);
        verify(session).persist(training1);
    }

    @Test
    public void testGet() {
        when(session.get(Training.class, training1.getId())).thenReturn(training1);
        assertEquals(training1, trainingDaoImpl.get(training1.getId()));
    }

    @Test
    public void testGetAll() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        trainings.add(new Training());
        Query query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.list()).thenReturn(trainings);
        assertEquals(trainings, trainingDaoImpl.getAll());
    }

    @Test
    public void testUpdateTrainersList() {
        Query query = mock(Query.class);
        List<Training> ts =  new ArrayList<>();
        ts.add(training1);
        ts.add(training2);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(ts.stream());
        assertEquals(List.of(trainer1, trainer2), trainingDaoImpl.updateTrainersList(trainee3));
    }

    @Test
    public void testGetAllTrainersAvalibleForTrainee() {
        Query query = mock(Query.class);
        List<Trainer> ts =  new ArrayList<>();
        ts.add(trainer2);
        ts.add(trainer1);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.of(training2));
        assertEquals(List.of(trainer1),
                trainingDaoImpl.getAllTrainersAvalibleForTrainee(trainee4, ts));
    }

    @Test
    public void testGetAllByTRAINERUsernameAndTrainingTypes() {
        when(userDao.getByUsername(trainer1_Username)).thenReturn(user1);
        Query query = mock(Query.class);
        Query query1 = mock(Query.class);
        Query query2 = mock(Query.class);
        when(userDao.getByUsername(trainer1_Username)).thenReturn(user1);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.of(training1));
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query1);
        when(query1.setParameter(anyString(), any())).thenReturn(query1);
        when(query1.getSingleResultOrNull()).thenReturn(null);
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query2);
        when(query2.setParameter(anyString(), any())).thenReturn(query2);
        when(query2.getSingleResultOrNull()).thenReturn(trainer1);
        assertEquals(List.of(training1), trainingDaoImpl.getAllByUsernameAndTrainingTypes(trainer1_Username, List.of(trainingType1), trainer1));
    }

    @Test
    public void testGetAllByTRAINEEUsernameAndTrainingTypes() {
        when(userDao.getByUsername(trainee3_Username)).thenReturn(user3);
        Query query = mock(Query.class);
        Query query1 = mock(Query.class);
        Query query2 = mock(Query.class);
        when(userDao.getByUsername(trainee3_Username)).thenReturn(user3);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.of(training1));
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query1);
        when(query1.setParameter(anyString(), any())).thenReturn(query1);
        when(query1.getSingleResultOrNull()).thenReturn(trainee3);
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(query2);
        when(query2.setParameter(anyString(), any())).thenReturn(query2);
        when(query2.getSingleResultOrNull()).thenReturn(null);
        assertEquals(List.of(training1), trainingDaoImpl.getAllByUsernameAndTrainingTypes(trainee3_Username, List.of(trainingType1), trainee3));
    }

}
