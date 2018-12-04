package com.olgaivancic.instateam.dao;

import com.olgaivancic.instateam.model.Project;

import java.util.List;

public interface ProjectDao {
    List<Project> findAll();
    Project findById(Long id);
    void save(Project project);
    void delete(Project project);
}
