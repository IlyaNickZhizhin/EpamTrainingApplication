package org.epam.mapper;

import org.epam.Reader;
import org.epam.dto.traineeDto.TraineeDto;
import org.epam.dto.trainerDto.TrainerDto;
import org.epam.dto.trainingDto.TrainingDto;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GymGeneralMapperTest {

    GymGeneralMapper gymGeneralMapper = GymGeneralMapper.INSTANCE;
    Reader reader = new Reader();
    @Test
    void testTraineeToTraineeDto() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainee trainee = reader.readTrainee("trainees/trainee1");
        assertEquals(trainee.getId(), gymGeneralMapper.traineeToTraineeDto(trainee).getId());
        assertEquals(trainee.getUser().getFirstName(), gymGeneralMapper.traineeToTraineeDto(trainee).getFirstname());
        assertEquals(trainee.getUser().getLastName(), gymGeneralMapper.traineeToTraineeDto(trainee).getLastname());
        assertEquals(trainee.getUser().getUsername(), gymGeneralMapper.traineeToTraineeDto(trainee).getUsername());
        assertEquals(trainee.getUser().getPassword(), gymGeneralMapper.traineeToTraineeDto(trainee).getPassword());
        assertEquals(trainee.getUser().isActive(), gymGeneralMapper.traineeToTraineeDto(trainee).isActive());
        assertEquals(trainee.getDateOfBirth(), gymGeneralMapper.traineeToTraineeDto(trainee).getDateOfBirth());
        assertEquals(trainee.getAddress(), gymGeneralMapper.traineeToTraineeDto(trainee).getAddress());
    }

    @Test
    void testTrainerToTrainerDto() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainer trainer = reader.readTrainer("trainers/trainer1");
        assertEquals(trainer.getId(), gymGeneralMapper.trainerToTrainerDto(trainer).getId());
        assertEquals(trainer.getUser().getFirstName(), gymGeneralMapper.trainerToTrainerDto(trainer).getFirstname());
        assertEquals(trainer.getUser().getLastName(), gymGeneralMapper.trainerToTrainerDto(trainer).getLastname());
        assertEquals(trainer.getUser().getUsername(), gymGeneralMapper.trainerToTrainerDto(trainer).getUsername());
        assertEquals(trainer.getUser().getPassword(), gymGeneralMapper.trainerToTrainerDto(trainer).getPassword());
        assertEquals(trainer.getUser().isActive(), gymGeneralMapper.trainerToTrainerDto(trainer).isActive());
        assertEquals(trainer.getSpecialization().getTrainingName(), gymGeneralMapper.trainerToTrainerDto(trainer).getSpecialization());
    }

    @Test
    void testTrainingToTrainingDto() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Training training = reader.readTraining("trainings/training1");
        assertEquals(training.getId(), gymGeneralMapper.trainingToTrainingDto(training).getId());
        assertEquals(training.getTrainingName(), gymGeneralMapper.trainingToTrainingDto(training).getTrainingName());
        assertEquals(training.getTrainingType().getTrainingName(), gymGeneralMapper.trainingToTrainingDto(training).getTrainingType());
        assertEquals(training.getTrainingDate(), gymGeneralMapper.trainingToTrainingDto(training).getTrainingDate());
        assertEquals(training.getDuration(), gymGeneralMapper.trainingToTrainingDto(training).getDuration());
        assertEquals(training.getTrainer().getUser().getUsername(), gymGeneralMapper.trainingToTrainingDto(training).getTrainer().getUsername());
        assertEquals(training.getTrainee().getUser().getUsername(), gymGeneralMapper.trainingToTrainingDto(training).getTrainee().getUsername());
    }

    @Test
    void testTraineeDtoToTrainee() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainee trainee = reader.readTrainee("trainees/trainee1");
        TraineeDto traineeDto = gymGeneralMapper.traineeToTraineeDto(trainee);
        assertEquals(trainee.getId(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getId());
        assertEquals(trainee.getUser().getFirstName(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getUser().getFirstName());
        assertEquals(trainee.getUser().getLastName(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getUser().getLastName());
        assertEquals(trainee.getUser().getUsername(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getUser().getUsername());
        assertEquals(trainee.getUser().getPassword(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getUser().getPassword());
        assertEquals(trainee.getUser().isActive(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getUser().isActive());
        assertEquals(trainee.getDateOfBirth(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getDateOfBirth());
        assertEquals(trainee.getAddress(), gymGeneralMapper.traineeDtoToTrainee(traineeDto).getAddress());
    }

    @Test
    void testTrainerDtoToTrainer() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Trainer trainer = reader.readTrainer("trainers/trainer1");
        TrainerDto trainerDto = gymGeneralMapper.trainerToTrainerDto(trainer);
        assertEquals(trainer.getId(), gymGeneralMapper.trainerDtoToTrainer(trainerDto).getId());
        assertEquals(trainer.getUser().getFirstName(), gymGeneralMapper.trainerDtoToTrainer(trainerDto).getUser().getFirstName());
        assertEquals(trainer.getUser().getLastName(), gymGeneralMapper.trainerDtoToTrainer(trainerDto).getUser().getLastName());
        assertEquals(trainer.getUser().getUsername(), gymGeneralMapper.trainerDtoToTrainer(trainerDto).getUser().getUsername());
        assertEquals(trainer.getUser().getPassword(), gymGeneralMapper.trainerDtoToTrainer(trainerDto).getUser().getPassword());
        assertEquals(trainer.getUser().isActive(), gymGeneralMapper.trainerDtoToTrainer(trainerDto).getUser().isActive());
        assertEquals(trainer.getSpecialization().getTrainingName(), gymGeneralMapper.trainerDtoToTrainer(trainerDto).getSpecialization().getTrainingName());
    }

    @Test
    void testTrainingDtoToTraining() {
        reader.setStartPath("src/test/resources/models/");
        reader.setEndPath(".json");
        Training training = reader.readTraining("trainings/training1");
        TrainingDto trainingDto = gymGeneralMapper.trainingToTrainingDto(training);
        assertEquals(training.getId(), gymGeneralMapper.trainingDtoToTraining(trainingDto).getId());
        assertEquals(training.getTrainingName(), gymGeneralMapper.trainingDtoToTraining(trainingDto).getTrainingName());
        assertEquals(training.getTrainingType().getTrainingName(), gymGeneralMapper.trainingDtoToTraining(trainingDto).getTrainingType().getTrainingName());
        assertEquals(training.getTrainingDate(), gymGeneralMapper.trainingDtoToTraining(trainingDto).getTrainingDate());
        assertEquals(training.getDuration(), gymGeneralMapper.trainingDtoToTraining(trainingDto).getDuration());
        assertEquals(training.getTrainer().getUser().getUsername(), gymGeneralMapper.trainingDtoToTraining(trainingDto).getTrainer().getUser().getUsername());
        assertEquals(training.getTrainee().getUser().getUsername(), gymGeneralMapper.trainingDtoToTraining(trainingDto).getTrainee().getUser().getUsername());
    }
}
