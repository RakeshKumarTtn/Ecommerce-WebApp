package com.org.ecom.service;

import com.org.ecom.dto.CustomerDto;
import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.Customer;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.utility.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ObjectConverter objectConverter;

    public CustomerDto addCustomer(CustomerDto customerDto) {
        Customer customer = objectConverter.dtoToEntity(customerDto);
        customer = customerRepository.save(customer);
        return objectConverter.entityToDto(customer);
    }

    public Set<Customer> getAllActiveCustomer() {
        List<Customer> customers = customerRepository.findAll();
        Set<Customer> customerHashSet = new HashSet<>();
        List<Long> customerIds = customerRepository.findAllCustomerId();
        customerHashSet = customerRepository.listOfActiveUser("Something", true, customerIds);
        return customerHashSet;
    }

    @Transactional
    public Customer deleteCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        customerRepository.deleteByEmail(!customer.getIsActive(), !customer.getIsDeleted(), email);
        return customer;
    }


    @Transactional
    public Customer deleteCustomerByUserName(String username) {
        Customer customer = customerRepository.findByUsername(username);
        if (customer.getIsActive())
            customerRepository.deleteSellerByUserName(!customer.getIsActive(), !customer.getIsDeleted(), username);
        return customer;
    }

    @Transactional
    @Modifying
    public UserDto updateCustomer(CustomerDto customerDto, String email) {
        Customer customer = customerRepository.findByEmail(email);
        customerRepository.delete(customer);
        return objectConverter.entityToDto(customerRepository.save(objectConverter.dtoToEntity(customerDto)));
    }
}
