package org.epam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
public class LiquibaseConfig {


    /*    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;
    @Bean
    public SpringLiquibase liquibase() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        //TODO не понимаю как найти драйвер для postgresql, hibernate находит, а liquibase нет
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("src/main/resources/db/changelog/db.changelog-master.xml");
        return liquibase;
    }*/
}
