package com.org.ecom.service;

import com.org.ecom.entities.Role;
import com.org.ecom.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Boolean findAuthority(String role) {
        if (roleRepository.findByAuthority(role).isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    public void save(Role role){
        roleRepository.save(role);
    }
}
