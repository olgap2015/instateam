package com.olgaivancic.instateam.service;

import com.olgaivancic.instateam.dao.CollaboratorDao;
import com.olgaivancic.instateam.dao.ProjectDao;
import com.olgaivancic.instateam.dao.RoleDao;
import com.olgaivancic.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Role findById(Long id) {
        return roleDao.findById(id);
    }

    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Override
    public void delete(Role role) {
        roleDao.delete(role);
    }
}
