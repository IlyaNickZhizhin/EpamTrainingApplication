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
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.epam.TestDatabaseInitializer.trainer1_Username;
import static org.epam.TestDatabaseInitializer.trainer2_FirstName;
import static org.epam.TestDatabaseInitializer.trainer2_LastName;
import static org.epam.TestDatabaseInitializer.trainer2_Username;
import static org.epam.TestDatabaseInitializer.user1;
import static org.epam.TestDatabaseInitializer.user2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserDaoImplTest {

    @Mock
    private SessionFactory sessionFactory = mock(SessionFactory.class);
    @Mock
    private Session session = mock(Session.class);

    @Mock
    private UsernameGenerator usernameGenerator = mock(UsernameGenerator.class);

    @Mock
    private PasswordGenerator passwordGenerator = mock(PasswordGenerator.class);

    @InjectMocks
    private UserDaoImpl userDao;

    Reader reader = new Reader();

    @BeforeEach
    public void setup() {
        initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
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
        User user = reader.readUser("users/user1");
        String username = reader.readUser("users/user1").getUsername();
        Query<User> query = mock(Query.class);
        when(session.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResultOrNull()).thenReturn(user);
        assertEquals(user1, userDao.getByUsername(username));
    }


    @Test
    public void testSetNewUser() {
        User user = new User();
        user.setFirstName(trainer2_FirstName);
        user.setLastName(trainer2_LastName);
        Query<User> query = mock(Query.class);
        when(session.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", trainer2_Username)).thenReturn(query);
        when(query.getSingleResultOrNull()).thenReturn(null);
        doNothing().when(session).persist(user);
        User newUser = userDao.setNewUser(trainer2_FirstName, trainer2_LastName);
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
    }

    @Test
    public void testSetNewUserUpToExisting() {
        User user = new User();
        User upToExist = new User(999, trainer2_FirstName, trainer2_LastName, trainer1_Username+"1", "", true);
        user.setFirstName(trainer2_FirstName);
        user.setLastName(trainer2_LastName);
        when(session.createQuery("from User where username = :username", User.class)).thenAnswer(
                invoks -> {
                    Query<User> query = mock(Query.class);
                    when(query.setParameter(anyString(), any())). thenAnswer(
                        querys -> {
                            String username = querys.getArgument(1);
                            if (username.equals(trainer2_Username)) {
                                when(query.getSingleResultOrNull()).thenReturn(user2);
                            } else {
                                if (username.equals(trainer2_Username+"1")) {
                                    when(query.getSingleResultOrNull()).thenReturn(upToExist);
                                } else
                                    when(query.getSingleResultOrNull()).thenReturn(null);
                            }
                            return query;
                        });
                    return query;
                }
        );
        doNothing().when(session).persist(user);
        User newUser = userDao.setNewUser(trainer2_FirstName, trainer2_LastName);
        assertEquals(user.getFirstName(), newUser.getFirstName());
        assertEquals(user.getLastName(), newUser.getLastName());
        assertEquals(trainer2_Username+"2", newUser.getUsername());
    }
}
