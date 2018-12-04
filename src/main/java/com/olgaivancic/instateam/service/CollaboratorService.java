package com.olgaivancic.instateam.service;

import com.olgaivancic.instateam.model.Collaborator;

import java.util.List;

public interface CollaboratorService {
    List<Collaborator> findAll();
    Collaborator findById(Long id);
    void save(Collaborator collaborator);
    void delete(Collaborator collaborator);
}
