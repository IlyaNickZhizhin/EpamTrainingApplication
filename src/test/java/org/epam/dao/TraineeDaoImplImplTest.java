package org.epam.dao;

import org.epam.Reader;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
import static org.mockito.MockitoAnnotations.initMocks;

public class TraineeDaoImplImplTest {

    @Mock
    private SessionFactory factory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);
    @Mock
    private UserDaoImpl userDao = mock(UserDaoImpl.class);
    @InjectMocks
    private TraineeDaoImpl traineeDaoImpl;

    private static Trainee trainee3;
    private static Trainee trainee4;
    private static User user3;
    Reader reader = new Reader();


    @BeforeEach
    public void setup() {
        initMocks(this);
        when(factory.getCurrentSession()).thenReturn(session);
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user3 = reader.readEntity("users/user3", User.class);
        trainee3 = reader.readEntity("trainees/trainee3", Trainee.class);
        trainee4 = reader.readEntity("trainees/trainee4", Trainee.class);
    }

    @Test
    public void testUpdate() {
        Trainee updatedTrainee = reader.readEntity("trainees/trainee3", Trainee.class);
        updatedTrainee.setAddress("new address");
        when(session.merge(updatedTrainee)).thenReturn(updatedTrainee);
        Trainee nt = traineeDaoImpl.update(1, updatedTrainee);
        assertEquals(nt.getAddress(), updatedTrainee.getAddress());
        verify(session).merge(updatedTrainee);
    }

    @Test
    public void testCreate() {
        Trainee trainee = new Trainee();
        doNothing().when(session).persist(trainee);
        traineeDaoImpl.create(trainee);
        verify(session).persist(trainee);
    }

    @Test
    public void testSave() {
        Trainee trainee = new Trainee();
        doNothing().when(session).persist(trainee);
        traineeDaoImpl.save(trainee);
        verify(session).persist(trainee);
    }

    @Test
    public void testGet() {
        Trainee trainee = trainee3;
        when(session.get(Trainee.class, trainee3.getId())).thenReturn(trainee);
        assertEquals(trainee, traineeDaoImpl.get(trainee3.getId()));
    }

    @Test
    public void testGetByUserId() {
        int userId = user3.getId();
        User user = user3;
        when(userDao.get(userId)).thenReturn(user);
        Trainee trainee = trainee3;
        Query<Trainee> query = mock(Query.class);
        when(factory.getCurrentSession()).thenReturn(session);
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResultOrNull()).thenReturn(trainee);
        assertEquals(trainee, traineeDaoImpl.getModelByUserId(userId));
    }

    @Test
    public void testGetAll() {
        List<Trainee> models = new ArrayList<>();
        models.add(trainee3);
        models.add(trainee4);
        Query<Trainee> query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.list()).thenReturn(models);
        assertEquals(models, traineeDaoImpl.getAll());
    }

    @Test
    public void testDelete() {
        int id = trainee3.getId();
        Trainee trainee = trainee3;
        when(session.get(Trainee.class, id)).thenReturn(trainee);
        doNothing().when(session).remove(trainee);
        traineeDaoImpl.delete(id);
        verify(session).remove(trainee);
    }




}
