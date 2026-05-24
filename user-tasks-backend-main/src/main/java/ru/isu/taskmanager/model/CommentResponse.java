package ru.isu.taskmanager.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {
    private Integer id;
    private String content;
    private LocalDateTime timestamp;
    private Integer taskId;
    private Integer userId;
    private String username;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.timestamp = comment.getTimestamp();
        this.taskId = comment.getTask().getId();
        this.userId = comment.getUser().getId();
        this.username = comment.getUser().getUsername();
    }
}
