package org.epam.dao;

import jakarta.persistence.EntityManager;
import org.epam.Reader;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.model.User;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    private EntityManager entityManager = mock(EntityManager.class);

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
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        user1 = reader.readEntity("users/user1", User.class);
    }

    @Test
    public void testCreate() {
        doNothing().when(entityManager).persist(user1);
        userDao.create(user1);
        verify(entityManager).persist(user1);
    }

    @Test
    public void testSave() {
        doNothing().when(entityManager).persist(user1);
        userDao.save(user1);
        verify(entityManager).persist(user1);
    }

    @Test
    public void testUpdate() {
        User user = user1;
        user.setFirstName("New name");
        when(entityManager.merge(user1)).thenReturn(user);
        userDao.update(1, user);
        verify(entityManager).merge(user);
    }

    @Test
    public void testDelete() {
        User user = user1;
        user.setId(user1.getId());
        when(entityManager.find(User.class, user1.getId())).thenReturn(user);
        doNothing().when(entityManager).remove(user);
        userDao.delete(user1.getId());
        verify(entityManager).remove(user);
    }

    @Test
    public void testGet() {
        when(entityManager.find(User.class, user1.getId())).thenReturn(user1);
        assertEquals(user1, userDao.get(user1.getId()).orElse(null));
    }

    @Test
    public void testGetByUsername() {
        User user = reader.readEntity("users/user1", User.class);
        String username = reader.readEntity("users/user1", User.class).getUsername();
        Query<User> query = mock(Query.class);
        when(entityManager.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(user);
        assertEquals(user1, userDao.getByUsername(username).orElse(null));
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
        when(entityManager.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", trainer2_Username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);
        User newUser = userDao.setNewUser(trainer2_FirstName, trainer2_LastName).orElse(null);
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
    }
}
