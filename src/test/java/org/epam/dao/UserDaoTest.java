package org.epam.dao;

import org.epam.Reader;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);

    @Spy
    private UsernameGenerator usernameGenerator = mock(UsernameGenerator.class);

    @Spy
    private PasswordGenerator passwordGenerator = mock(PasswordGenerator.class);

    @InjectMocks
    private UserDao userDao;

    Reader reader = new Reader();
    User user1;

    @BeforeEach
    public void setup() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user1 = reader.readEntity("users/user1", User.class);
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
        when(session.merge(user1)).thenReturn(user);
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
        User user = reader.readEntity("users/user1", User.class);
        String username = reader.readEntity("users/user1", User.class).getUsername();
        Query<User> query = mock(Query.class);
        when(session.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResultOrNull()).thenReturn(user);
        assertEquals(user1, userDao.getByUsername(username));
    }


    @Test
    public void testSetNewUser() {
        User user2 = reader.readEntity("users/user2", User.class);
        String trainer2_FirstName = user2.getFirstName();
        String trainer2_LastName = user2.getLastName();
        String trainer2_Username = user2.getUsername();
        User user = new User();
        user.setFirstName(trainer2_FirstName);
        user.setLastName(trainer2_LastName);
        Query<User> query = mock(Query.class);
        when(session.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", trainer2_Username)).thenReturn(query);
        when(query.getSingleResultOrNull()).thenReturn(null);
        User newUser = userDao.setNewUser(trainer2_FirstName, trainer2_LastName);
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
    }
}
