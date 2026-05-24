package ru.isu.taskmanager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequest {
    private String content;
    private Integer task_id;
    private Integer user_id;
}
