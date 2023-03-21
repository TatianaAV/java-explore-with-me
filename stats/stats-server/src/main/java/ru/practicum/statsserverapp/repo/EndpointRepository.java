package ru.practicum.statsserverapp.repo;

import dto.EndpointDtoOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statsserverapp.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    @Query("select  new dto.EndpointDtoOutput(en.app, en.uri, count(en.id)) from Endpoint as en where en.timestamp between :start and :end group by en.app,en.uri order by count (en.id) desc ")
    List<EndpointDtoOutput> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("select  new dto.EndpointDtoOutput(en.app, en.uri, count(en.id)) from Endpoint as en where en.timestamp between :start and :end group by en.app,en.uri, en.id order by count (en.id) desc ")
    List<EndpointDtoOutput> findDistinctByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("select  new dto.EndpointDtoOutput(en.app, en.uri, count(en.id)) from Endpoint as en where en.uri in :uris and en.timestamp between :start and :end group by en.app,en.uri, en.id order by count (en.id) desc ")
    List<EndpointDtoOutput> findDistinctByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select  new dto.EndpointDtoOutput(en.app, en.uri, count (en.id))  from Endpoint as en where en.timestamp between :start and :end and en.uri in :uris group by en.app, en.uri order by count (en.id) desc ")
    List<EndpointDtoOutput> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);
}
