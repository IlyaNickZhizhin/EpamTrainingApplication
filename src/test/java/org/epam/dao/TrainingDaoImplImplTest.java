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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class TrainingDaoImplImplTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);
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
        user1 = reader.readEntity("users/user1", User.class);
        user3 = reader.readEntity("users/user3", User.class);
        trainee3 = reader.readEntity("trainees/trainee3", Trainee.class);
        trainee4 = reader.readEntity("trainees/trainee4", Trainee.class);
        trainer1 = reader.readEntity("trainers/trainer1", Trainer.class);
        trainer2 = reader.readEntity("trainers/trainer2", Trainer.class);
        training1 = reader.readEntity("trainings/training1", Training.class);
        training2 = reader.readEntity("trainings/training2", Training.class);
        trainingType1 = reader.readEntity("trainingtypes/trainingType1", TrainingType.class);
        trainee3_Username = user3.getUsername();
        trainer1_Username = user1.getUsername();
    }


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
        assertEquals(training1, trainingDaoImpl.get(training1.getId()).orElse(null));
    }

    @Test
    public void testGetAll() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        trainings.add(new Training());
        Query<Training> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.list()).thenReturn(trainings);
        assertEquals(trainings, trainingDaoImpl.getAll().orElse(null));
    }

    @Test
    public void testGetAllTrainersAvalibleForTrainee() {
        Query<Training> query = mock(Query.class);
        List<Trainer> ts =  new ArrayList<>();
        ts.add(trainer2);
        ts.add(trainer1);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.of(training2));
        assertEquals(List.of(trainer1),
                trainingDaoImpl.getAllTrainersAvalibleForTrainee(trainee4, ts).orElse(null));
    }

    @Test
    public void testGetAllByTRAINERUsernameAndTrainingTypes() {
        Query<Training> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.of(training1));
        assertEquals(List.of(training1),
                trainingDaoImpl.getAllByUsernameAndTrainingTypes(List.of(trainingType1), trainer1).orElse(null));
    }

    @Test
    public void testGetAllByTRAINEEUsernameAndTrainingTypes() {
        Query<Training> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultStream()).thenReturn(Stream.of(training1));
        assertEquals(List.of(training1),
                trainingDaoImpl.getAllByUsernameAndTrainingTypes(List.of(trainingType1), trainee3).orElse(null));
    }

}
