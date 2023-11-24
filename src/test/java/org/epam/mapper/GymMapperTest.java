package org.epam.mapper;

import org.epam.Reader;
import org.epam.dto.TraineeDto;
import org.epam.dto.TrainerDto;
import org.epam.dto.TrainingDto;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GymMapperTest {

    GymMapper gymMapper = GymMapper.INSTANCE;
    Reader reader = new Reader();
    @Test
    void testTraineeToTraineeDto() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainee trainee = reader.readTrainee("trainees/trainee1");
        assertEquals(trainee.getId(), gymMapper.traineeToTraineeDto(trainee).getId());
        assertEquals(trainee.getUser().getFirstName(), gymMapper.traineeToTraineeDto(trainee).getFirstname());
        assertEquals(trainee.getUser().getLastName(), gymMapper.traineeToTraineeDto(trainee).getLastname());
        assertEquals(trainee.getUser().getUsername(), gymMapper.traineeToTraineeDto(trainee).getUsername());
        assertEquals(trainee.getUser().getPassword(), gymMapper.traineeToTraineeDto(trainee).getPassword());
        assertEquals(trainee.getUser().isActive(), gymMapper.traineeToTraineeDto(trainee).isActive());
        assertEquals(trainee.getDateOfBirth(), gymMapper.traineeToTraineeDto(trainee).getDateOfBirth());
        assertEquals(trainee.getAddress(), gymMapper.traineeToTraineeDto(trainee).getAddress());
    }

    @Test
    void testTrainerToTrainerDto() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainer trainer = reader.readTrainer("trainers/trainer1");
        assertEquals(trainer.getId(), gymMapper.trainerToTrainerDto(trainer).getId());
        assertEquals(trainer.getUser().getFirstName(), gymMapper.trainerToTrainerDto(trainer).getFirstname());
        assertEquals(trainer.getUser().getLastName(), gymMapper.trainerToTrainerDto(trainer).getLastname());
        assertEquals(trainer.getUser().getUsername(), gymMapper.trainerToTrainerDto(trainer).getUsername());
        assertEquals(trainer.getUser().getPassword(), gymMapper.trainerToTrainerDto(trainer).getPassword());
        assertEquals(trainer.getUser().isActive(), gymMapper.trainerToTrainerDto(trainer).isActive());
        assertEquals(trainer.getSpecialization().getTrainingName(), gymMapper.trainerToTrainerDto(trainer).getSpecialization());
    }

    @Test
    void testTrainingToTrainingDto() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Training training = reader.readTraining("trainings/training1");
        assertEquals(training.getId(), gymMapper.trainingToTrainingDto(training).getId());
        assertEquals(training.getTrainingName(), gymMapper.trainingToTrainingDto(training).getTrainingName());
        assertEquals(training.getTrainingType().getTrainingName(), gymMapper.trainingToTrainingDto(training).getTrainingType());
        assertEquals(training.getTrainingDate(), gymMapper.trainingToTrainingDto(training).getTrainingDate());
        assertEquals(training.getDuration(), gymMapper.trainingToTrainingDto(training).getDuration());
        assertEquals(training.getTrainer().getUser().getUsername(), gymMapper.trainingToTrainingDto(training).getTrainer().getUsername());
        assertEquals(training.getTrainee().getUser().getUsername(), gymMapper.trainingToTrainingDto(training).getTrainee().getUsername());
    }

    @Test
    void testTraineeDtoToTrainee() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainee trainee = reader.readTrainee("trainees/trainee1");
        TraineeDto traineeDto = gymMapper.traineeToTraineeDto(trainee);
        assertEquals(trainee.getId(), gymMapper.traineeDtoToTrainee(traineeDto).getId());
        assertEquals(trainee.getUser().getFirstName(), gymMapper.traineeDtoToTrainee(traineeDto).getUser().getFirstName());
        assertEquals(trainee.getUser().getLastName(), gymMapper.traineeDtoToTrainee(traineeDto).getUser().getLastName());
        assertEquals(trainee.getUser().getUsername(), gymMapper.traineeDtoToTrainee(traineeDto).getUser().getUsername());
        assertEquals(trainee.getUser().getPassword(), gymMapper.traineeDtoToTrainee(traineeDto).getUser().getPassword());
        assertEquals(trainee.getUser().isActive(), gymMapper.traineeDtoToTrainee(traineeDto).getUser().isActive());
        assertEquals(trainee.getDateOfBirth(), gymMapper.traineeDtoToTrainee(traineeDto).getDateOfBirth());
        assertEquals(trainee.getAddress(), gymMapper.traineeDtoToTrainee(traineeDto).getAddress());
    }

    @Test
    void testTrainerDtoToTrainer() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainer trainer = reader.readTrainer("trainers/trainer1");
        TrainerDto trainerDto = gymMapper.trainerToTrainerDto(trainer);
        assertEquals(trainer.getId(), gymMapper.trainerDtoToTrainer(trainerDto).getId());
        assertEquals(trainer.getUser().getFirstName(), gymMapper.trainerDtoToTrainer(trainerDto).getUser().getFirstName());
        assertEquals(trainer.getUser().getLastName(), gymMapper.trainerDtoToTrainer(trainerDto).getUser().getLastName());
        assertEquals(trainer.getUser().getUsername(), gymMapper.trainerDtoToTrainer(trainerDto).getUser().getUsername());
        assertEquals(trainer.getUser().getPassword(), gymMapper.trainerDtoToTrainer(trainerDto).getUser().getPassword());
        assertEquals(trainer.getUser().isActive(), gymMapper.trainerDtoToTrainer(trainerDto).getUser().isActive());
        assertEquals(trainer.getSpecialization().getTrainingName(), gymMapper.trainerDtoToTrainer(trainerDto).getSpecialization().getTrainingName());
    }

    @Test
    void testTrainingDtoToTraining() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Training training = reader.readTraining("trainings/training1");
        TrainingDto trainingDto = gymMapper.trainingToTrainingDto(training);
        assertEquals(training.getId(), gymMapper.trainingDtoToTraining(trainingDto).getId());
        assertEquals(training.getTrainingName(), gymMapper.trainingDtoToTraining(trainingDto).getTrainingName());
        assertEquals(training.getTrainingType().getTrainingName(), gymMapper.trainingDtoToTraining(trainingDto).getTrainingType().getTrainingName());
        assertEquals(training.getTrainingDate(), gymMapper.trainingDtoToTraining(trainingDto).getTrainingDate());
        assertEquals(training.getDuration(), gymMapper.trainingDtoToTraining(trainingDto).getDuration());
        assertEquals(training.getTrainer().getUser().getUsername(), gymMapper.trainingDtoToTraining(trainingDto).getTrainer().getUser().getUsername());
        assertEquals(training.getTrainee().getUser().getUsername(), gymMapper.trainingDtoToTraining(trainingDto).getTrainee().getUser().getUsername());
    }
}
