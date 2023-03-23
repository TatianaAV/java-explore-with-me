package ru.practicum.statsserverapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.statsserverapp.model.App;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    Optional<App> findByName(String app);
}