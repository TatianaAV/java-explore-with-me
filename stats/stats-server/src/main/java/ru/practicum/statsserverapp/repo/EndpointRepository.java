package ru.practicum.statsserverapp.repo;

import dto.ViewStatsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statsserverapp.model.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    @Query("select  new dto.ViewStatsDto(en.app.name, en.uri, count(en.id)) from Endpoint as en where en.timestamp between :start and :end group by en.app.name, en.uri order by count (en.id) desc ")
    List<ViewStatsDto> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("select  new dto.ViewStatsDto(en.app.name, en.uri, count(en.id)) from Endpoint as en where en.timestamp between :start and :end group by en.app.name, en.uri, en.id order by count (en.id) desc")
    List<ViewStatsDto> findDistinctByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("select new dto.ViewStatsDto(en.app.name, en.uri, count(en.id)) " +
            "from Endpoint as en where en.uri in :uris and en.timestamp between :start and :end " +
            "group by en.app.name, en.uri, en.id order by count(en.id) desc")
    List<ViewStatsDto> findDistinctByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris, Pageable pageable);

    @Query("select  new dto.ViewStatsDto(en.app.name, en.uri, count (en.id))  from Endpoint as en where en.timestamp between :start and :end and en.uri in :uris group by en.app.name, en.uri order by count (en.id) desc ")
    List<ViewStatsDto> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);
}
