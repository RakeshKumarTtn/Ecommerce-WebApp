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

   /* @Test
    public void testSeller() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Rakesh");
        userEntity.setMiddleName("Kumar");
        userEntity.setLastName("Prajapati");
        userEntity.setEmail("rakeshp@ttn.com");
        userEntity.setPassword("123456");

        Address address = new Address();
        address.setAddressLine("Lane 4");
        address.setCityName("Delhi");
        address.setStateName("New Delhi");
        address.setZipCode("110018");
        address.setCountryName("India");

        userEntity.addAddress(address);


        Seller seller = new Seller();
        seller.setCompanyContact("9953256940");
        seller.setCompanyName("Reliance");
        seller.setGstIn("rrrrrr");

        Role role2 = new Role();
        role2.setAuthority("USER");

        user.addRole(role2);

        //seller.setUser(user);

        sellerRepository.save(seller);
    }*/

   /* @Test
    void testCustomer() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Rakesh");
        userEntity.setMiddleName("Kumar");
        userEntity.setLastName("Prajapati");
        userEntity.setEmail("rakeshp@ttn.com");
        userEntity.setPassword("123456");

        Address address = new Address();
        address.setAddressLine("Lane 4");
        address.setCityName("Delhi");
        address.setStateName("New Delhi");
        address.setZipCode("110018");
        address.setCountryName("India");

        userEntity.addAddress(address);

        Address address1 = new Address();
        address1.setAddressLine("Lane 5");
        address1.setCityName("Delhi_6");
        address1.setStateName("New Delhi");
        address1.setZipCode("110019");
        address1.setCountryName("India");

        userEntity.addAddress(address1);

        Customer customer = new Customer();
        customer.setContact("9953256940");

        *//*Role role1 = new Role();
        role1.setAuthority("ADMIN");

        userEntity.addRole(role1);

        Role role2 = new Role();
        role2.setAuthority("USER");

        userEntity.addRole(role2);

        customer.setUser(userEntity);

        customerRepository.save(customer);*//*
    }*/


    /*@Test
    public void testManyToMany() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Rakesh");
        userEntity.setMiddleName("Kumar");
        userEntity.setLastName("Prajapati");
        userEntity.setEmail("rakeshp@ttn.com");
        userEntity.setPassword("123456");

        Address address = new Address();
        address.setAddressLine("Lane_68");
        address.setCityName("FARIDABAD");
        address.setStateName("UP");
        address.setZipCode("121002");
        address.setCountryName("India");
        userEntity.addAddress(address);

        Role role1 = new Role();
        role1.setAuthority("ADMIN");

        userEntity.addRole(role1);


        Role role2 = new Role();
        role2.setAuthority("USER");

        userEntity.addRole(role2);

        userRepository.save(userEntity);
    }*/
}
