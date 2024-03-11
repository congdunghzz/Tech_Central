package com.example.techcentral.dao;

import com.example.techcentral.enums.UserRole;
import com.example.techcentral.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Long> findIdByEmail(String email);

    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByUserRole(UserRole role);
}
