package com.org.ecom;

import com.org.ecom.constant.APPConstant;
import com.org.ecom.entities.Address;
import com.org.ecom.entities.Role;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.repository.RolesRepository;
import com.org.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

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
            role.setAuthority("ROLE_ADMIN");

            roles.add(role);
            user.setRoles(roles);

            userRepository.save(user);
            System.out.println("Total users saved::" + userRepository.count());
        }
    }
}