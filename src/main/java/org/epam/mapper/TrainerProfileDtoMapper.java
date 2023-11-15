package org.epam.mapper;

import lombok.AllArgsConstructor;
import org.epam.config.Storage;
import org.epam.dao.UserDao;
import org.epam.dto.TrainerProfile;
import org.epam.model.User;
import org.epam.model.gymModel.Trainer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainerProfileDtoMapper {

    ModelMapper modelMapper;
    UserDao userDao;
    public Trainer convertToEntity(TrainerProfile trainerProfileDto) {
        User user = userDao.get(trainerProfileDto.getUsername());
        modelMapper.typeMap(TrainerProfile.class, Trainer.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getId(), Trainer::setId);
                    mapper.map(src -> src.getSpecialization(), Trainer::setSpecialization);
                    mapper.map(src -> user.getId(), Trainer::setUserId);
                });
        return modelMapper.map(trainerProfileDto, Trainer.class);
    }

    public TrainerProfile convertToDto(Trainer trainer) {
        User user = userDao.getById(trainer.getUserId());
        modelMapper.typeMap(Trainer.class, TrainerProfile.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getId(), TrainerProfile::setId);
                    mapper.map(src -> src.getSpecialization(), TrainerProfile::setSpecialization);
                    mapper.map(src -> user.getUsername(), TrainerProfile::setUsername);
                    mapper.map(src -> user.getFirstName(), TrainerProfile::setFirstName);
                    mapper.map(src -> user.getLastName(), TrainerProfile::setLastName);
                    mapper.map(src -> user.getPassword(), TrainerProfile::setPassword);
                    mapper.map(src -> user.isActive(), TrainerProfile::setActive);
                });
        return modelMapper.map(trainer, TrainerProfile.class);
    }
}
