package com.shoes.assignment.service;

import com.shoes.assignment.model.UserRole;
import com.shoes.assignment.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRole saveUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public Optional<UserRole> getUserRoleById(Long userRoleId) {
        return userRoleRepository.findById(userRoleId);
    }

    public void deleteUserRole(Long userRoleId) {
        userRoleRepository.deleteById(userRoleId);
    }
}
