package com.ironhack.claudiamidterm.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NotNull
    protected String name;
    @NotNull
    protected String username;
    @NotNull
    protected String password;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public Set<Role> getRoles() {return roles;}
    public void setRoles(Set<Role> roles) {this.roles = roles;}
    public void addRole(Role role){this.roles.add(role);}

    @Override
    public String toString() {
        return  id + "\n" +
                username + "\n" +
                name + "\n" ;
    }

}
