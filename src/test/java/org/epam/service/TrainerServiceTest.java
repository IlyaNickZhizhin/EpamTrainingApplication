package org.epam.service;


import org.epam.Reader;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.TrainerDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

class TrainerServiceTest {

    @Mock
    private TrainerDaoImpl mockTrainerDaoImpl = mock(TrainerDaoImpl.class);

    @Mock
    private UserDaoImpl mockUserDao = mock(UserDaoImpl.class);

    @Mock
    private PasswordChecker mockPasswordChecker = mock(PasswordChecker.class);

    @InjectMocks
    private TrainerService trainerService;

    Reader reader = new Reader();

    @BeforeEach
    public void setUp() {
        initMocks(this);
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
    }
}
