package org.epam.mainservice.repository;

import org.epam.mainservice.model.User;
import org.epam.mainservice.model.gymModel.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer>{
    Optional<Trainee> findByUser(User user);
}
