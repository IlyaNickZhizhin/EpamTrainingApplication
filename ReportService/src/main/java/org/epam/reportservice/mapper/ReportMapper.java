package org.epam.reportservice.mapper;

import org.epam.reportservice.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.TrainerWorkloadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    @Mapping(target = "map", ignore = true)
    TrainerWorkloadResponse RequestToResponse(TrainerWorkloadRequest request);

}
