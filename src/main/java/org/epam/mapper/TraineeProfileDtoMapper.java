package org.epam.mapper;

import lombok.AllArgsConstructor;
import org.epam.dao.UserDao;
import org.epam.dto.TraineeProfile;
import org.epam.dto.TrainerProfile;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TraineeProfileDtoMapper {

    ModelMapper modelMapper;
    UserDao userDao;

    public Trainee convertToEntity(TraineeProfile traineeProfileDto) {
        User user = userDao.get(traineeProfileDto.getUsername());
        modelMapper.typeMap(TraineeProfile.class, Trainee.class)
                .addMappings(mapper -> {
                    mapper.map(src -> user.getId(), Trainee::setUserId);
                    mapper.map(src -> src.getId(), Trainee::setId);
                    mapper.map(src -> src.getAddress(), Trainee::setAddress);
                    mapper.map(src -> src.getDateOfBirth(), Trainee::setDateOfBirth);
                });
        return modelMapper.map(traineeProfileDto, Trainee.class);
    }

    public TrainerProfile convertToDto(Trainee trainee) {
        User user = userDao.getById(trainee.getUserId());
        modelMapper.typeMap(Trainee.class, TraineeProfile.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getId(), TraineeProfile::setId);
                    mapper.map(src -> src.getDateOfBirth(), TraineeProfile::setDateOfBirth);
                    mapper.map(src -> src.getAddress(), TraineeProfile::setAddress);
                    mapper.map(src -> user.getUsername(), TraineeProfile::setUsername);
                    mapper.map(src -> user.getFirstName(), TraineeProfile::setFirstName);
                    mapper.map(src -> user.getLastName(), TraineeProfile::setLastName);
                    mapper.map(src -> user.getPassword(), TraineeProfile::setPassword);
                    mapper.map(src -> user.isActive(), TraineeProfile::setActive);
                });
        return modelMapper.map(trainee, TrainerProfile.class);
    }

}
