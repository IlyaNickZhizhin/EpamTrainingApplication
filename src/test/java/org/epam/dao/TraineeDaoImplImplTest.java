package org.epam.dao;

import org.epam.exceptions.ResourceNotFoundException;
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

import static org.epam.TestDatabaseInitializer.trainee3;
import static org.epam.TestDatabaseInitializer.trainee4;
import static org.epam.TestDatabaseInitializer.user3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);
    @Mock
    private UserDaoImpl userDao = mock(UserDaoImpl.class);
    @InjectMocks
    private TraineeDaoImpl traineeDaoImpl;


    @BeforeEach
    public void setup() {
        initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testUpdate() {
        Trainee trainee = trainee3;
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setId(trainee.getId());
        updatedTrainee.setAddress("New address");
        when(session.get(Trainee.class, 1)).thenReturn(trainee);
        traineeDaoImpl.update(1, updatedTrainee);
        assertEquals(trainee.getAddress(), updatedTrainee.getAddress());
        verify(session).merge(trainee);
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
        Query query = mock(Query.class);
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        Trainee trainee = trainee3;
        when(query.getSingleResult()).thenReturn(trainee);
        assertEquals(trainee, traineeDaoImpl.getModelByUserId(userId));
    }

    @Test
    public void testGetAll() {
        List<Trainee> models = new ArrayList<>();
        models.add(trainee3);
        models.add(trainee4);
        Query query = mock(Query.class);
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

    @Test
    public void testDeleteThrowsException() {
        int id = 99999999;
        when(session.get(Trainee.class, id)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> traineeDaoImpl.delete(id));
    }

    @Test
    public void testGetThrowsException() {
        int id = 99999;
        when(session.get(Trainee.class, id)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> traineeDaoImpl.get(id));
    }

    @Test
    public void testGetByUserIdThrowsException() {
        int userId = 1;
        when(userDao.get(userId)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> traineeDaoImpl.getModelByUserId(userId));
    }


}
