package com.olgaivancic.instateam.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters of length!")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$") // Pattern allows only alphanumeric characters and white space
    private String name;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull
    @Size(min = 3, max = 300, message = "Description must be 3 to 300 characters of length!")
    private String description;

    @NotNull
    @Size(max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$") // Pattern allows only alphanumeric characters and white space
    private String status;

    @NotNull
    @Size(min = 1, message = "At least one role must be assigned to the project!")
    @ManyToMany
    private List<Role> rolesNeeded = new ArrayList<>();

    @ManyToMany
    private List<Collaborator> collaborators = new ArrayList<>();

    public Project() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", rolesNeeded=" + rolesNeeded +
                ", collaborators=" + collaborators +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) &&
                Objects.equals(name, project.name) &&
                Objects.equals(description, project.description) &&
                Objects.equals(status, project.status) &&
                Objects.equals(rolesNeeded, project.rolesNeeded) &&
                Objects.equals(collaborators, project.collaborators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, rolesNeeded, collaborators);
    }

}
