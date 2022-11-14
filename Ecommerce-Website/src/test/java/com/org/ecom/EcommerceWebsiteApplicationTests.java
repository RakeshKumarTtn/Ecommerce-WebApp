package com.org.ecom;

import com.org.ecom.entities.*;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EcommerceWebsiteApplicationTests {

    @Autowired
    CustomerRepository customerRepository;

   /* @Autowired
    SellerRepository sellerRepository;*/

    @Autowired
    UserRepository userRepository;

    @Test
    public void testSeller() {
        User user = new User();
        user.setFirstName("Rakesh");
        user.setMiddleName("Kumar");
        user.setLastName("Prajapati");
        user.setEmail("rakeshp@ttn.com");
        user.setPassword("123456");

        Address address = new Address();
        address.setAddressLine("Lane 4");
        address.setCityName("Delhi");
        address.setStateName("New Delhi");
        address.setZipCode("110018");
        address.setCountryName("India");

        user.addAddress(address);

/*
        Seller seller = new Seller();
        seller.setCompanyContact("9953256940");
        seller.setCompanyName("Reliance");
        seller.setGstIn("rrrrrr");

        Role role2 = new Role();
        role2.setAuthority("USER");

        user.addRole(role2);

        //seller.setUser(user);

        sellerRepository.save(seller);*/
    }

    @Test
    void testCustomer() {
        User user = new User();
        user.setFirstName("Rakesh");
        user.setMiddleName("Kumar");
        user.setLastName("Prajapati");
        user.setEmail("rakeshp@ttn.com");
        user.setPassword("123456");

        Address address = new Address();
        address.setAddressLine("Lane 4");
        address.setCityName("Delhi");
        address.setStateName("New Delhi");
        address.setZipCode("110018");
        address.setCountryName("India");

        user.addAddress(address);

        Address address1 = new Address();
        address1.setAddressLine("Lane 5");
        address1.setCityName("Delhi_6");
        address1.setStateName("New Delhi");
        address1.setZipCode("110019");
        address1.setCountryName("India");

        user.addAddress(address1);

        Customer customer = new Customer();
        customer.setContact("9953256940");

        Role role1 = new Role();
        role1.setAuthority("ADMIN");

        user.addRole(role1);

        Role role2 = new Role();
        role2.setAuthority("USER");

        user.addRole(role2);

        customer.setUser(user);

        customerRepository.save(customer);
    }


    @Test
    public void testManyToMany() {
        User user = new User();
        user.setFirstName("Rakesh");
        user.setMiddleName("Kumar");
        user.setLastName("Prajapati");
        user.setEmail("rakeshp@ttn.com");
        user.setPassword("123456");

        Address address = new Address();
        address.setAddressLine("Lane_68");
        address.setCityName("FARIDABAD");
        address.setStateName("UP");
        address.setZipCode("121002");
        address.setCountryName("India");
        user.addAddress(address);

        Role role1 = new Role();
        role1.setAuthority("ADMIN");

        user.addRole(role1);


        Role role2 = new Role();
        role2.setAuthority("USER");

        user.addRole(role2);

        userRepository.save(user);
    }
}
