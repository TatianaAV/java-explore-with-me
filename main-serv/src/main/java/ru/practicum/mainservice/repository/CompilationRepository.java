package ru.practicum.mainservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.CompilationTitle;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByTitle_Id(Long compId);

    List<Compilation> findAllByTitleIn(List<CompilationTitle> title);

    @Modifying
    @Query(value = "delete from Compilation where title.id = ?1")
    void deleteAllByTitle_Id(Long id);
}
