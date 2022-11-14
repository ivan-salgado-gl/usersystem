package com.bci.project.exercise.usersystem.repository;

import com.bci.project.exercise.usersystem.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    boolean existsByEmail(final String email);

    Optional<User> findByEmail(final String email);

}
