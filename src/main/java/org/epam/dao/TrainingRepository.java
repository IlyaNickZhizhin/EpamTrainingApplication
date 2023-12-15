package org.epam.dao;

import org.epam.model.gymModel.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
}
