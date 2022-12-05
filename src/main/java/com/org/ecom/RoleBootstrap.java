package com.org.ecom;

import com.org.ecom.entity.registration.Role;
import com.org.ecom.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.org.ecom.enums.RoleLevel.*;

@Component
public class RoleBootstrap implements ApplicationRunner {

    @Autowired
    RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String roleSeller = String.valueOf(SELLER);
        String roleCustomer = String.valueOf(CUSTOMER);
        String roleAdmin = String.valueOf(ADMIN);


        if(roleService.findAuthority(roleAdmin)){
            Role role1=new Role();
            role1.setAuthority(roleAdmin);
            roleService.save(role1);
        }


        if(roleService.findAuthority(roleCustomer)) {
            Role role2 = new Role();
            role2.setAuthority(roleCustomer);
            roleService.save(role2);
        }


        if(roleService.findAuthority(roleSeller)) {
            Role role3 = new Role();
            role3.setAuthority(roleSeller);
            roleService.save(role3);
        }
    }
}
