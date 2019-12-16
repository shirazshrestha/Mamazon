package com.project.onlinestore.security.service;

import com.project.onlinestore.security.domain.Role;
import com.project.onlinestore.security.respository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role getById(long id) {
        return roleRepository.findById(id).get();
    }
}
