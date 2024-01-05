package org.epam.gymservice.repository;

import org.epam.gymservice.model.gymModel.Training;
import org.epam.gymservice.model.gymModel.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {

    @Query("SELECT t FROM TrainingType t")
    List<TrainingType> getAllTrainingTypes();

}
