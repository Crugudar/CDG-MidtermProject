package com.ironhack.claudiamidterm.model;

import com.fasterxml.jackson.annotation.*;
import com.ironhack.claudiamidterm.enums.*;

import javax.persistence.*;

@Entity
@Table
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    public Role() {}

    public Role(UserRole role, User user) {
        this.role = role;
        this.user = user;
    }

    /** Getters & Setters **/
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", authority='" + role + '\'';
    }
}
