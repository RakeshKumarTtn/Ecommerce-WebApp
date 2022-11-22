package com.org.ecom;

import com.org.ecom.constant.APPConstant;
import com.org.ecom.entities.Address;
import com.org.ecom.entities.Role;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.enums.RoleLevel;
import com.org.ecom.repository.RoleRepository;
import com.org.ecom.repository.UserRepository;
import com.org.ecom.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    RoleRepository rolesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String roleSeller = String.valueOf(RoleLevel.SELLER);
        String roleCustomer = String.valueOf(RoleLevel.CUSTOMER);
        String roleAdmin = String.valueOf(RoleLevel.ADMIN);

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
            UserEntity user = new UserEntity();
            user.setUsername("Rakesh_TTN");
            user.setFirstName("Rakesh");
            user.setMiddleName("Kumar");
            user.setLastName("Prajapati");
            user.setEmail("rakesh.kumar3@tothenew.com");
            user.setMobile(9718122312L);
            user.setPassword(bCryptPasswordEncoder.encode("Admin@123"));
            user.setPasswordUpdateDate(new Date());
            user.setIsDeleted(APPConstant.IS_DELETED);
            user.setIsActive(!APPConstant.IS_ACTIVE);
            user.setIsExpired(APPConstant.IS_EXPIRED);
            user.setIsLocked(APPConstant.IS_LOCKED);
            user.setInvalidAttemptCount(APPConstant.INVALID_ATTEMPT_COUNT);

            Set<Address> addresses = new HashSet<>();
            Address address = new Address();
            address.setAddressLine("house no. 123");
            address.setCityName("Faridabad");
            address.setCountryName("India");
            address.setLabel("HomeTown");
            address.setStateName("UP");
            address.setZipCode("121003");
            addresses.add(address);

            address.setUserEntity(user);

            user.setUserAddress(addresses);

            List<Role> roles = new ArrayList<>();
            Role role = new Role();
            role.setAuthority(RoleLevel.ADMIN.toString());

            roles.add(role);
            user.setRoles(roles);

            userRepository.save(user);
            System.out.println("Total users saved::" + userRepository.count());
        }
    }
}