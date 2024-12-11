package com.todo.todoapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = Task.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    public static final String TABLE_NAME = "task";

    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // "Many" se refere à classe Task, "One" se refere ao user, então várias tasks pertencem a um user
    // A primeira palavra se refere a onde estou agora, nesse caso a Task
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "description", length = 200, nullable = false)
    @NotBlank
    private String description;
}
