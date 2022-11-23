package com.org.ecom;

import com.org.ecom.entities.*;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.repository.UserRepository;
import com.org.ecom.security.CustomJWTRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EcommerceWebsiteApplicationTests {

    @Autowired
    UserRepository userRepository;

    //
    @Test
    public void testSeller() {

        Seller seller = new Seller();

        seller.setFirstName("Rakesh");
        seller.setMiddleName("Kumar");
        seller.setLastName("Prajapati");
        seller.setEmail("rakeshp@ttn.com");
        seller.setUsername("rakesh");
        seller.setPassword("123456");

        Address address = new Address();
        address.setAddressLine("Lane 4");
        address.setCityName("Delhi");
        address.setStateName("New Delhi");
        address.setZipCode("110018");
        address.setCountryName("India");

        seller.addAddress(address);


        seller.setCompanyContact("9953256940");
        seller.setCompanyName("Reliance");
        seller.setGstin("rrrrrr");

        Role role2 = new Role();
        role2.setAuthority("USER");

        seller.addRole(role2);


        userRepository.save(seller);
    }

    @Test
    public void testManyToMany() {

        Customer customer=new Customer();

        customer.setFirstName("Rakesh");
        customer.setMiddleName("Kumar");
        customer.setLastName("Prajapati");
        customer.setEmail("amrit@ttn.com");
        customer.setPassword("123456");

        customer.setContact("9953256940");

        Address address = new Address();
        address.setAddressLine("Lane_68");
        address.setCityName("FARIDABAD");
        address.setStateName("UP");
        address.setZipCode("121002");
        address.setCountryName("India");
        customer.addAddress(address);

        Role role1 = new Role();
        role1.setAuthority("ADMIN");

        customer.addRole(role1);


        Role role2 = new Role();
        role2.setAuthority("USER");

        customer.addRole(role2);

        userRepository.save(customer);
    }

}
