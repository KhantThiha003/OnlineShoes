package com.shoes.assignment.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserRole")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public UserRole() {
    }

    public UserRole(Long userRoleID, User user, Role role) {
        this.userRoleID = userRoleID;
        this.user = user;
        this.role = role;
    }

    public Long getUserRoleID() {
        return userRoleID;
    }

    public void setUserRoleID(Long userRoleID) {
        this.userRoleID = userRoleID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleID=" + userRoleID +
                ", user=" + user +
                ", role=" + role +
                '}';
    }
}
