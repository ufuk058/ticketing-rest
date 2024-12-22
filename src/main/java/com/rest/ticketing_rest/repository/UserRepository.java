package com.rest.ticketing_rest.repository;

import com.rest.ticketing_rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);

    User findByUserNameAndIsDeleted(String username, Boolean deleted);

    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String role, Boolean deleted);
}
