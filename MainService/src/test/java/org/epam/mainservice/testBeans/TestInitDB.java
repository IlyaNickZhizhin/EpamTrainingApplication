package org.epam.mainservice.testBeans;

import jakarta.annotation.PostConstruct;
import org.epam.mainservice.model.Role;
import org.epam.mainservice.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("test")
public class TestInitDB {

    @Autowired
    TrainingTypeRepository trainingTypeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Value("${spring.liquibase.enabled}")
    private boolean enabled;


    @PostConstruct
    @Profile("test")
    void initDatabase() {
        if (!enabled) {
            List<TrainingType.TrainingName> list =  TrainingType.TrainingName.getTrainingNames();
                for (TrainingType.TrainingName trainingName : list) {
                    TrainingType trainingType = new TrainingType();
                    trainingType.setTrainingName(trainingName);
                    trainingTypeRepository.save(trainingType);
                }
            List<Role.Authority> roleList = Role.Authority.getAuthorities();
                for (Role.Authority authority : roleList) {
                    Role role = new Role();
                    role.setAuthority(authority);
                    roleRepository.save(role);
            }
        }
    }
}
