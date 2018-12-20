package com.olgaivancic.instateam.model;

import javax.persistence.Entity;
import java.util.List;


// Wrapper class that enables to save a list of Collaborators while submitting a form

public class CollaboratorListWrapper {
    private List<Collaborator> collaborators;

    public CollaboratorListWrapper(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public CollaboratorListWrapper() {
    }

    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
    }

    public void addCollaborator(Collaborator collaborator) {
        this.collaborators.add(collaborator);
    }
}
