package com.todo.todoapp.repositories;

import com.todo.todoapp.models.Task;
import com.todo.todoapp.models.projection.TaskProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<TaskProjection> findByUser_Id(Long id);

    //@Query(value = "SELECT t FROM Task t WHERE t.user.id = :user_id")
    //List<Task> findByUser_Id(@Param("user_id") Long id);

//    @Query(value = "SELECT * FROM task t WHERE t.user_id = :user_id", nativeQuery = true)
//    List<Task> findByUser_Id(@Param("user_id") Long id);
}
