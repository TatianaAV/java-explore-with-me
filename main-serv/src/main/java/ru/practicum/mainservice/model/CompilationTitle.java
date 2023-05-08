package ru.practicum.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations_title", schema = "public")
public class CompilationTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private Boolean pined;

    public CompilationTitle(String title, Boolean pined) {
        this.title = title;
        this.pined = pined;
    }
}
