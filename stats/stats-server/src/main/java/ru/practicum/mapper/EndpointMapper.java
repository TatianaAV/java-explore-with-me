package ru.practicum.mapper;

import dto.EndpointDto;
import dto.EndpointDtoOutput;
import org.mapstruct.Mapper;
import ru.practicum.model.Endpoint;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EndpointMapper {

    EndpointDtoOutput toEndpointOutput(Endpoint endpoint);

    Endpoint toEndpoint(EndpointDto endpoint);

    EndpointDto toEndpointDto(Endpoint save);
}
