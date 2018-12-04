package com.olgaivancic.instateam.dao;

import com.olgaivancic.instateam.model.Role;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {

    @Autowired SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> findAll() {
        Session session = sessionFactory.openSession();
        List<Role> categories = session.createCriteria(Role.class).list();
        session.close();
        return categories;
    }

    @Override
    public Role findById(Long id) {
        Session session= sessionFactory.openSession();
        Role role = session.get(Role.class, id);
        session.close();
        return role;
    }
}
