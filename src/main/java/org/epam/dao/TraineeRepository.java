package org.epam.dao;

import org.epam.model.gymModel.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Integer>{
}
