package com.org.ecom.service;

import com.org.ecom.entity.registration.Role;
import com.org.ecom.repository.registration.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void save(Role role) {
        roleRepository.save(role);
    }

    public boolean findAuthority(String role) {
        if (roleRepository.findByAuthority(role).isPresent()) {
            return false;
        } else {
            return true;
        }
    }

}
