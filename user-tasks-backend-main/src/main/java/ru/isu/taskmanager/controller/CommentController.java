package ru.isu.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.isu.taskmanager.model.Comment;
import ru.isu.taskmanager.model.CommentRequest;
import ru.isu.taskmanager.model.CommentResponse;
import ru.isu.taskmanager.model.Task;
import ru.isu.taskmanager.model.User;
import ru.isu.taskmanager.repository.CommentRepository;
import ru.isu.taskmanager.repository.TaskRepository;
import ru.isu.taskmanager.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentRepository.findAllWithRelations().stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments/task/{taskId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByTaskId(@PathVariable Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            return ResponseEntity.notFound().build();
        }
        List<CommentResponse> comments = commentRepository.findByTaskIdWithRelations(taskId).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Integer commentId) {
        return commentRepository.findByIdWithRelations(commentId)
                .map(comment -> ResponseEntity.ok(new CommentResponse(comment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/addComment")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest commentRequest) {
        Task task = taskRepository.findById(commentRequest.getTask_id()).orElse(null);
        User user = userRepository.findById(commentRequest.getUser_id()).orElse(null);
        if (task == null || user == null || commentRequest.getContent() == null || commentRequest.getContent().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Comment comment = new Comment(commentRequest.getContent(), task, user);
        Comment saved = commentRepository.save(comment);
        return commentRepository.findByIdWithRelations(saved.getId())
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponse(c)))
                .orElse(ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponse(saved)));
    }

    @PostMapping("/updateComment/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Integer commentId,
            @RequestBody CommentRequest commentRequest
    ) {
        Comment comment = commentRepository.findByIdWithRelations(commentId).orElse(null);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        if (commentRequest.getContent() == null || commentRequest.getContent().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
        return ResponseEntity.ok(new CommentResponse(comment));
    }

    @PostMapping("/deleteComment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
