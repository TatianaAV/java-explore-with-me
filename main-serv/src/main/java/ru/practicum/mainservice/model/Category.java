package ru.practicum.mainservice.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotBlank(message = "Имя отсутствует")
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
