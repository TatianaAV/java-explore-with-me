package ru.practicum.mainservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.dto.ParticipationRequest.ParticipationRequestDto;
import ru.practicum.mainservice.model.ParticipationRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "id", source = "request.id")
    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requester", source = "request.requester.id")
    ParticipationRequestDto toRequestDto(ParticipationRequest request);

    List<ParticipationRequestDto> toListRequestDto(List<ParticipationRequest> listRequest);
}
