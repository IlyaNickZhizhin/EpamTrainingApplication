package org.epam.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HibernateConfig.class)
public class HibernateConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testSessionFactory() {
        LocalSessionFactoryBean sessionFactory = context.getBean(LocalSessionFactoryBean.class);
        assertNotNull(sessionFactory);
    }

    @Test
    public void testDataSource() {
        DataSource dataSource = context.getBean(DataSource.class);
        assertNotNull(dataSource);
    }

    @Test
    public void testTransactionManager() {
        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);
        assertNotNull(transactionManager);
    }
}
