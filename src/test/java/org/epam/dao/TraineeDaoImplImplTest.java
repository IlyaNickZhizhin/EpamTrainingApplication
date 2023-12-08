package org.epam.dao;

import jakarta.persistence.EntityManager;
import org.epam.Reader;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeDaoImplImplTest {

    @Mock
    private EntityManager entityManager = mock(EntityManager.class);
    @Mock
    private UserDao userDao = mock(UserDao.class);
    @InjectMocks
    private TraineeDaoImpl traineeDaoImpl;

    private static Trainee trainee3;
    private static Trainee trainee4;
    private static User user3;
    Reader reader = new Reader();


    @BeforeEach
    public void setup() {
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
        when(entityManager.merge(updatedTrainee)).thenReturn(updatedTrainee);
        Trainee nt = traineeDaoImpl.update(1, updatedTrainee).orElse(new Trainee());
        assertEquals(nt.getAddress(), updatedTrainee.getAddress());
        verify(entityManager).merge(updatedTrainee);
    }

    @Test
    public void testCreate() {
        Trainee trainee = new Trainee();
        doNothing().when(entityManager).persist(trainee);
        traineeDaoImpl.create(trainee);
        verify(entityManager).persist(trainee);
    }

    @Test
    public void testSave() {
        Trainee trainee = new Trainee();
        doNothing().when(entityManager).persist(trainee);
        traineeDaoImpl.save(trainee);
        verify(entityManager).persist(trainee);
    }

    @Test
    public void testGet() {
        Trainee trainee = trainee3;
        when(entityManager.find(Trainee.class, trainee3.getId())).thenReturn(trainee);
        assertEquals(trainee, traineeDaoImpl.get(trainee3.getId()).orElse(null));
    }

    @Test
    public void testGetByUserId() {
        int userId = user3.getId();
        User user = user3;
        when(userDao.get(userId)).thenReturn(Optional.of(user));
        Trainee trainee = trainee3;
        Query<Trainee> query = mock(Query.class);
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainee);
        assertEquals(trainee, traineeDaoImpl.getModelByUserId(userId).orElse(null));
    }

    @Test
    public void testGetAll() {
        List<Trainee> models = new ArrayList<>();
        models.add(trainee3);
        models.add(trainee4);
        Query<Trainee> query = mock(Query.class);
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(models);
        assertEquals(models, traineeDaoImpl.getAll().orElse(null));
    }

    @Test
    public void testDelete() {
        int id = trainee3.getId();
        Trainee trainee = trainee3;
        when(entityManager.find(Trainee.class, id)).thenReturn(trainee);
        doNothing().when(entityManager).remove(trainee);
        traineeDaoImpl.delete(id);
        verify(entityManager).remove(trainee);
    }




}
