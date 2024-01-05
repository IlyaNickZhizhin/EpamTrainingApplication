package org.epam.mainservice.repository;

import org.epam.mainservice.model.User;
import org.epam.mainservice.model.gymModel.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUser(User user);
}
