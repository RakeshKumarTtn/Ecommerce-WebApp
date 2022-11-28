package com.org.ecom;

import com.org.ecom.constant.APPConstant;
import com.org.ecom.entities.*;
import com.org.ecom.enums.RoleLevel;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.repository.RoleRepository;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.repository.UserRepository;
import com.org.ecom.security.CustomizedAuditorAware;
import com.org.ecom.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Bootstrap implements ApplicationRunner {

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

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;


    Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        logger.info("Bootstrap :: run execution started.");

        if (userRepository.count() < 1) {

            logger.debug("Bootstrap :: run creating an admin user");
            UserEntity user = new UserEntity();
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
            logger.debug(user.getFirstName() + " " + user.getLastName() + " created.");

            logger.debug("Bootstrap :: run created an admin user");


            Customer customer = new Customer();
            customer.setContact(9718122312L);

            Seller seller = new Seller();
            seller.setGstin("GU-096IKL");
            seller.setCompanyContact(9718122312L);
            seller.setCompanyName("To The New Pvt. Ltd.");

            Set<Address> addresses = new HashSet<>();
            Address address = new Address();
            address.setAddressLine("house no. 123");
            address.setCityName("Faridabad");
            address.setCountryName("India");
            address.setLabel("HomeTown");
            address.setStateName("UP");
            address.setZipCode("121003");
            addresses.add(address);

            address.setUser(user);

            user.setUserAddress(addresses);

            Role role = new Role();
            role.setAuthority(RoleLevel.ADMIN.toString());

            customizedAuditorAware.setName(user.getFirstName() + " " + user.getLastName());

            user.addRole(role);
            userRepository.save(user);
            System.out.println("Total users saved::" + userRepository.count());
            logger.info("BootstrapCommandLineRunner::run execution ended.");
        }
    }
}