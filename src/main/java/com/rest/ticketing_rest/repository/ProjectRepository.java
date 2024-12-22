package com.rest.ticketing_rest.repository;

import com.rest.ticketing_rest.entity.Project;
import com.rest.ticketing_rest.entity.User;
import com.rest.ticketing_rest.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByProjectCode(String projectCode);
    List<Project> findAllByAssignedManager(User manager);
    List<Project> findAllByProjectStatusIsNotAndAssignedManager(Status status, User manager);
}
