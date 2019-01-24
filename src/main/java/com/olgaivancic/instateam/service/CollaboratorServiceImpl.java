package com.olgaivancic.instateam.service;

import com.olgaivancic.instateam.dao.CollaboratorDao;
import com.olgaivancic.instateam.dao.ProjectDao;
import com.olgaivancic.instateam.model.Collaborator;
import com.olgaivancic.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    @Autowired
    private CollaboratorDao collaboratorDao;

    @Autowired
    private ProjectDao projectDao;

    @Override
    public List<Collaborator> findAll() {
        return collaboratorDao.findAll();
    }

    @Override
    public Collaborator findById(Long id) {
        return collaboratorDao.findById(id);
    }

    @Override
    public void save(Collaborator collaborator) {
        collaboratorDao.save(collaborator);
    }

    @Override
    public void delete(Collaborator collaborator) {
        // Make a set of collaborators that are involved in projects
        Set<Collaborator> allCollaboratorsInvolvedInProjects = new HashSet<>();
        List<Project> projects = projectDao.findAll();
        for (Project project : projects) {
            if (project.getCollaborators().size() > 0) {
                allCollaboratorsInvolvedInProjects.addAll(project.getCollaborators());
            }
        }
        // If the collaborator is involved in at least one project
        if (allCollaboratorsInvolvedInProjects.contains(collaborator)) {
            // Make a list of such projects
            List<Project> projectsThatInvolveThisCollaborator = projects.stream()
                    .filter(project -> project.getCollaborators().contains(collaborator))
                    .collect(Collectors.toList());
            // Delete the collaborator from the list of collaborators for each of those projects
            if (projectsThatInvolveThisCollaborator.size() > 0) {
                for (Project project : projectsThatInvolveThisCollaborator) {
                    List<Collaborator> collaborators = project.getCollaborators();
                    if (collaborators.size() > 0) {
                        for (int i = collaborators.size() - 1; i >= 0; i--) {
                            Collaborator col = collaborators.get(i);
                            if (col.equals(collaborator)) {
                                collaborators.remove(col);
                                projectDao.save(project);
                            }
                        }
                    }
                }
            }
        }
        collaboratorDao.delete(collaborator);
    }
}
