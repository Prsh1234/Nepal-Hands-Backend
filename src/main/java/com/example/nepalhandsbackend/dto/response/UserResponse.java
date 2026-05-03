package com.example.nepalhandsbackend.dto.response;

import com.example.nepalhandsbackend.states.Role;

import java.util.Set;

public class UserResponse {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles;

    public UserResponse() {}

    public UserResponse(int id, String email, String firstName, String lastName, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}