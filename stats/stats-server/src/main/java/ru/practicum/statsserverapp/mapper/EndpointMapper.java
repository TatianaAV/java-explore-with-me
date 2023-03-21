package ru.practicum.statsserverapp.mapper;

import dto.EndpointDto;
import org.mapstruct.Mapper;
import ru.practicum.statsserverapp.model.Endpoint;

@Mapper(componentModel = "spring")
public interface EndpointMapper {

    Endpoint toEndpoint(EndpointDto endpoint);

    EndpointDto toEndpointDto(Endpoint save);
}
