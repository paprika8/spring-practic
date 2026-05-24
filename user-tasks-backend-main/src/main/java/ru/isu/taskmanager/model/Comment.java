package ru.isu.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="task")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private Task task;

    @ManyToOne
    @JsonIgnore
    private User user;

    @NotBlank
    private String content;

    @NotBlank
    private LocalDateTime timestamp;

    public Comment(String content, Task task, User user) {
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.task = task;
        this.user = user;
    }
}
