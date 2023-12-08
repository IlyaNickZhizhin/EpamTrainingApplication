/*package org.epam.config;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LiquibaseConfig.class)
public class TestLiquibaseConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testLiquibase() {
        SpringLiquibase liquibase = context.getBean(SpringLiquibase.class);
        assertNotNull(liquibase);
    }
}*/
