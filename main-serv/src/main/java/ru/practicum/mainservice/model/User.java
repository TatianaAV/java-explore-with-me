package ru.practicum.mainservice.model;

import lombok.*;
import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @Column(name = "email", nullable = false, length = 300, unique = true)
    private String email;
}