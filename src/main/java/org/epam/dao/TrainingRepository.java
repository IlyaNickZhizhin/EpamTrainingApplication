package org.epam.dao;

import org.epam.model.gymModel.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TrainingRepository extends JpaRepository<Training, Integer> {
}
