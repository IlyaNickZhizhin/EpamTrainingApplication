package org.epam.mainservice.testBeans;

import org.epam.mainservice.model.gymModel.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
}