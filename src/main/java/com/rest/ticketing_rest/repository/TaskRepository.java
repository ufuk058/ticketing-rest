package com.rest.ticketing_rest.repository;

import com.rest.ticketing_rest.entity.Project;
import com.rest.ticketing_rest.entity.Task;
import com.rest.ticketing_rest.entity.User;
import com.rest.ticketing_rest.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectCode = ?1 AND t.taskStatus <> 'COMPLETE'")
    int totalNonCompletedTasks(String projectCode);

    @Query(value = "SELECT COUNT(*) " +
            "FROM tasks t JOIN projects p ON t.project_id=p.id " +
            "WHERE p.project_code=?1 AND t.task_status='COMPLETE'", nativeQuery = true)
    int totalCompletedTasks(String projectCode);

    List<Task> findAllByProject(Project project);

    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User employee);

    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User employee);

    Task findByTaskCode(String taskCode);
}
