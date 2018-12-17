package com.olgaivancic.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters of length!")
    @Pattern(regexp = "^[a-zA-Z0-9\\s'\\.-]*$", message = "Only letters, \".\"  \"''\" and \"-\" are allowed!") // Pattern allows only letters, white space, ".", "'" and "-"
    private String name;

    @NotNull
    @ManyToOne
    private Role role;

    public Collaborator() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
