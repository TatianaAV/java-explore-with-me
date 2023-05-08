package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import ru.practicum.mainservice.model.CompilationTitle;

import java.util.Optional;

public interface CompilationTitleRepository extends JpaRepository<CompilationTitle, Long> {
    Page<CompilationTitle> findByPined(@Nullable Boolean pined, Pageable pageable);

    Optional<CompilationTitle> findByTitle(String nameCompilation);
}