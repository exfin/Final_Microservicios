package com.users.microuser.dao;

import com.users.microuser.enums.Role;
import com.users.microuser.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByRole(Role role);
}
