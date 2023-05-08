package ru.practicum.statsserverapp.mapper;

import dto.EndpointDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.practicum.statsserverapp.model.App;
import ru.practicum.statsserverapp.model.Endpoint;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EndpointMapper {

    @Mapping(target = "app", source = "app")
    @Mapping(target = "id", ignore = true)
    Endpoint toEndpoint(EndpointDto endpoint, App app);

    @Mapping(target = "app", source = "app.name")
    EndpointDto toEndpointDto(Endpoint save);
}
