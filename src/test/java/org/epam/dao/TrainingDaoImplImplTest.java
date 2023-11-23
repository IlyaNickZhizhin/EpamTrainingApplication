package org.epam.dao;

import org.epam.Reader;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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

    static User user1;
    static User user3;
    static Trainee trainee3;
    static Trainee trainee4;
    static Trainer trainer1;
    static Trainer trainer2;
    static String trainee3_Username;
    static String trainer1_Username;
    static Training training1;
    static Training training2;
    static TrainingType trainingType1;


    @BeforeAll
    public static void init() {
        Reader reader = new Reader();
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user1 = reader.readUser("users/user1");
        user3 = reader.readUser("users/user2");
        trainee3 = reader.readTrainee("trainees/trainee1");
        trainee4 = reader.readTrainee("trainees/trainee2");
        trainer1 = reader.readTrainer("trainers/trainer1");
        trainer2 = reader.readTrainer("trainers/trainer2");
        training1 = reader.readTraining("trainings/training1");
        training2 = reader.readTraining("trainings/training2");
        trainingType1 = reader.readType("trainingtypes/trainingType1");
        trainee3_Username = new String(user3.getUsername());
        trainer1_Username = new String(user1.getUsername());
    }


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
