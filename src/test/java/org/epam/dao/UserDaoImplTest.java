package org.epam.dao;

import org.hibernate.query.Query;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.epam.Supplier.trainer2_FirstName;
import static org.epam.Supplier.trainer2_LastName;
import static org.epam.Supplier.user1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserDaoImplTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);

    @Mock
    private UsernameGenerator usernameGenerator = mock(UsernameGenerator.class);

    @Mock
    private PasswordGenerator passwordGenerator = mock(PasswordGenerator.class);

    private UserDaoImpl userDao = new UserDaoImpl(sessionFactory, usernameGenerator, passwordGenerator);

    @BeforeEach
    public void setup() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testCreate() {
        doNothing().when(session).persist(user1);
        userDao.create(user1);
        verify(session).persist(user1);
    }

    @Test
    public void testSave() {
        doNothing().when(session).persist(user1);
        userDao.save(user1);
        verify(session).persist(user1);
    }

    @Test
    public void testUpdate() {
        User user = user1;
        user.setFirstName("New name");
        when(session.get(User.class, 1)).thenReturn(user);
        userDao.update(1, user);
        verify(session).merge(user);
    }

    @Test
    public void testDelete() {
        User user = user1;
        user.setId(user1.getId());
        when(session.get(User.class, user1.getId())).thenReturn(user);
        doNothing().when(session).remove(user);
        userDao.delete(user1.getId());
        verify(session).remove(user);
    }

    @Test
    public void testGet() {
        when(session.get(User.class, user1.getId())).thenReturn(user1);
        assertEquals(user1, userDao.get(user1.getId()));
    }

    @Test
    public void testGetByUsername() {
        String username = user1.getUsername();
        Query query = mock(Query.class);
        when(session.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResultOrNull()).thenReturn(user1);
        assertEquals(user1, userDao.getByUsername(username));
    }


    @Test
    public void testSetNewUser() {
        User user = new User();
        user.setFirstName(trainer2_FirstName);
        user.setLastName(trainer2_LastName);
        doNothing().when(session).persist(user);
        User newUser = userDao.setNewUser(trainer2_FirstName, trainer2_LastName);
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
    }
}
