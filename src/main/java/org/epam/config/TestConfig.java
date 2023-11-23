package org.epam.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp.BasicDataSource;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static jakarta.persistence.GenerationType.IDENTITY;

@Configuration
@Profile("test")
public class TestConfig {


    @Value("${spring.datasource.url.test}")
    private String testUrl;
    @Value("${spring.datasource.username.test}")
    private String username;
    @Value("${spring.datasource.password.test}")
    private String password;
    @Value("${spring.datasource.driver-class-name.test}")
    private String driver;
    @Value("${spring.liquibase.enabled.test}")
    private boolean enabled;
    @Value("${spring.datasource.hibernate.dialect.test}")
    private String dialect;
    @Value("${spring.datasource.hibernate.ddl-auto.test}")
    private String ddl_auto;
    @Value("${spring.datasource.hibernate.show-sql.test}")
    private String show_sql;
    @Value("${spring.datasource.hibernate.current_session_context_class.test}")
    private String session_cntx;

    @Bean
    @Profile("test")
    public SpringLiquibase liquibase() {
        if (!enabled) {
            return null;
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(testUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("db/changelog/db.test-changelog-master.xml");
        return liquibase;
    }

    @Bean
    @Profile("test")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(testUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }


    @Bean
    @Profile("test")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(User.class.getPackageName(),
                TrainingType.class.getPackageName(),
                Trainer.class.getPackageName(),
                Trainee.class.getPackageName(),
                Training.class.getPackageName());
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", ddl_auto);
        hibernateProperties.setProperty("hibernate.dialect", dialect);
        hibernateProperties.setProperty("hibernate.show-sql", show_sql);
        return hibernateProperties;
    }

    private final void fillTrainingTypes(){
        List<TrainingType.TrainingName> list = Arrays.asList(TrainingType.TrainingName.values());
        for (TrainingType.TrainingName trainingName : list) {
            TrainingType trainingType = new TrainingType();
            trainingType.setTrainingName(trainingName);
        }
    }

}