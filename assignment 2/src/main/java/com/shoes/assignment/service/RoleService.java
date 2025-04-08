package com.shoes.assignment.service;

import com.shoes.assignment.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getRoleById(Long id);
    Optional<Role> getRoleByName(String name);
    Role saveRole(Role role);
}
