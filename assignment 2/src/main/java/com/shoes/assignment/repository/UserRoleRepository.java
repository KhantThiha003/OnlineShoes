package com.shoes.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shoes.assignment.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
