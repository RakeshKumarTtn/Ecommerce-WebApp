package com.org.ecom;

import com.org.ecom.entity.registration.Address;
import com.org.ecom.entity.registration.Admin;
import com.org.ecom.entity.registration.Role;
import com.org.ecom.enums.RoleLevel;
import com.org.ecom.repository.registration.RoleRepository;
import com.org.ecom.repository.registration.UserRepository;
import com.org.ecom.security.CustomizedAuditorAware;
import com.org.ecom.service.RoleService;
import com.org.ecom.utility.APPConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.org.ecom.enums.RoleLevel.*;

@Component
public class AdminBootstrap implements ApplicationRunner {

    @Autowired
    RoleRepository rolesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleService roleService;

    @Autowired
    CustomizedAuditorAware customizedAuditorAware;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String roleSeller = String.valueOf(SELLER);
        String roleCustomer = String.valueOf(CUSTOMER);
        String roleAdmin = String.valueOf(ADMIN);


        if (roleService.findAuthority(roleAdmin)) {
            Role role1 = new Role();
            role1.setAuthority(roleAdmin);
            roleService.save(role1);
        }


        if (roleService.findAuthority(roleCustomer)) {
            Role role2 = new Role();
            role2.setAuthority(roleCustomer);
            roleService.save(role2);
        }


        if (roleService.findAuthority(roleSeller)) {
            Role role3 = new Role();
            role3.setAuthority(roleSeller);
            roleService.save(role3);
        }

        if (userRepository.count() < 1) {
            Admin user = new Admin();
            user.setUsername("Rakesh_TTN");
            user.setFirstName("Rakesh");
            user.setMiddleName("Kumar");
            user.setLastName("Prajapati");
            user.setEmail("rakesh.kumar3@tothenew.com");
            user.setPassword(bCryptPasswordEncoder.encode("Admin@123"));
            user.setPasswordUpdateDate(new Date());
            user.setIsDeleted(APPConstant.IS_DELETED);
            user.setIsActive(!APPConstant.IS_ACTIVE);
            user.setIsExpired(APPConstant.IS_EXPIRED);
            user.setIsLocked(APPConstant.IS_LOCKED);
            user.setInvalidAttemptCount(APPConstant.INVALID_ATTEMPT_COUNT);


            Address address = new Address();
            address.setAddressLine("house no. 123");
            address.setCityName("Faridabad");
            address.setCountryName("India");
            address.setLabel("HomeTown");
            address.setStateName("UP");
            address.setZipCode("121003");


            address.setUser(user);

            user.addAddress(address);

            Role role = new Role();
            role.setAuthority(RoleLevel.ADMIN.toString());

            user.addRole(role);

            customizedAuditorAware.setName(user.getFirstName());

            userRepository.save(user);
            System.out.println("Total users saved::" + userRepository.count());
        }
    }
}