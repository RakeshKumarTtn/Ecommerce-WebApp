package com.org.ecom.service;

import com.org.ecom.entity.registration.Role;
import com.org.ecom.repository.registration.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    //Method for saving the role in the database
    public void save(Role role) {
        roleRepository.save(role);
    }

    //Method for finding the authorities of particular Role
    public boolean findAuthority(String role) {
        if (roleRepository.findByAuthority(role).isPresent()) {
            return false;
        } else {
            return true;
        }
    }
}
