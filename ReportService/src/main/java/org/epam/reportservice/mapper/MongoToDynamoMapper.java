package org.epam.reportservice.mapper;

import org.epam.reportservice.model.Workload;
import org.epam.reportservice.model.WorkloadDynamo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
@Component
@Mapper(componentModel = "spring")
public interface MongoToDynamoMapper {

    @Mapping(source = "active", target = "active", qualifiedByName = "stringToBoolean")
    Workload dynamoToMongo(WorkloadDynamo workload);

    @Mapping(source = "active", target = "active", qualifiedByName = "booleanToString")
    WorkloadDynamo mongoToDynamo(Workload workload);


    @Named("stringToBoolean")
    default boolean stringToBoolean(String active) {
        return Boolean.getBoolean(active);
    }

    @Named("booleanToString")
    default String stringToBoolean(boolean isActive) {
        return String.valueOf(isActive);
    }

}
