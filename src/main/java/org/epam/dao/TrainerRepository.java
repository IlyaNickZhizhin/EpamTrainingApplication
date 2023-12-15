package org.epam.dao;

import org.epam.model.gymModel.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
}
