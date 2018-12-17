package com.olgaivancic.instateam.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters of length!")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$") // Pattern allows only alphanumeric characters and white space
    private String name;

    @NotNull(message = "This field can't be empty!")
    @Size(max = 300)
    private String description;

    @NotNull
    @Size(max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$") // Pattern allows only alphanumeric characters and white space
    private String status;

    @NotNull(message = "At least one role must be assigned to the project!")
    @ManyToMany
    private List<Role> rolesNeeded = new ArrayList<>();

    @ManyToMany
    private List<Collaborator> collaborators = new ArrayList<>();

    public Project() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Role> getRolesNeeded() {
        return rolesNeeded;
    }

    public void setRolesNeeded(List<Role> rolesNeeded) {
        this.rolesNeeded = rolesNeeded;
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

}
