package org.epam.gymservice.repository;

import org.epam.gymservice.model.User;
import org.epam.gymservice.model.gymModel.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Optional<Trainer> findByUser(User user);
}
