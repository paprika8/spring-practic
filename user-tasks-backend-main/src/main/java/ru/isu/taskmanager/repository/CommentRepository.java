package ru.isu.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.isu.taskmanager.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.task JOIN FETCH c.user")
    List<Comment> findAllWithRelations();

    @Query("SELECT c FROM Comment c JOIN FETCH c.task JOIN FETCH c.user WHERE c.id = :id")
    Optional<Comment> findByIdWithRelations(@Param("id") Integer id);

    @Query("SELECT c FROM Comment c JOIN FETCH c.task JOIN FETCH c.user WHERE c.task.id = :taskId")
    List<Comment> findByTaskIdWithRelations(@Param("taskId") Integer taskId);
}
