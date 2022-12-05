package com.org.ecom.service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.org.ecom.entity.registration.Admin;
import com.org.ecom.entity.registration.Customer;
import com.org.ecom.entity.registration.Seller;
import com.org.ecom.entity.registration.UserEntity;
import com.org.ecom.exception.UserNotFoundException;
import com.org.ecom.repository.registration.CustomerRepository;
import com.org.ecom.repository.registration.SellerRepository;
import com.org.ecom.repository.registration.UserRepository;
import com.org.ecom.validation.AdminActivationValidations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminActivationValidations adminActivationValidations;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    MessageSource messageSource;

    private Long[] l = {};

    Logger logger = LoggerFactory.getLogger(AdminService.class);

    public MappingJacksonValue listAllCustomers(String email) {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "userId"));
        List<Customer> customers;

        if (customerRepository.existsByEmail(email)) {
            customers = Arrays.asList(customerRepository.findByEmail(email).get());

        } else {
            Page<Customer> customerPage = customerRepository.findAll(pageable);
            customers = customerPage.getContent();
        }
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customers);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId",
                "firstName", "middleName", "lastName", "email", "isActive");

        FilterProvider filters = new SimpleFilterProvider().addFilter("CustomersFilter", filter);

        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    public MappingJacksonValue listAllSellers(String email) {

        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "userId"));
        List<Seller> sellers;

        if (sellerRepository.existsByEmail(email)) {
            sellers = Arrays.asList(sellerRepository.findByEmail(email).get());
        } else {
            Page<Seller> sellerPage = sellerRepository.findAll(pageable);
            sellers = sellerPage.getContent();
        }

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(sellers);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId",
                "firstName", "middleName", "lastName", "email", "isActive",
                "companyName", "companyContact", "userAddress");

        FilterProvider filters = new SimpleFilterProvider().addFilter("SellersFilter", filter);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    public MappingJacksonValue getAllRegisteredCustomers(Integer pageNo, Integer pageSize, String sortBy) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        List<Customer> customersList = customerRepository.findAll(pageable).toList();

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customersList);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId",
                "firstName", "middleName", "lastName", "email", "isActive");

        FilterProvider filters = new SimpleFilterProvider().addFilter("CustomersFilter", filter);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    public MappingJacksonValue getAllRegisteredSellers(Integer pageNo, Integer pageSize, String sortBy) {
        PageRequest pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        List<Seller> sellersList = sellerRepository.findAll(pageable).toList();

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(sellersList);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId",
                "firstName", "middleName", "lastName", "email", "isActive",
                "companyName", "companyContact", "userAddress");

        FilterProvider filters = new SimpleFilterProvider().addFilter("SellersFilter", filter);
        mappingJacksonValue.setFilters(filters);
        return mappingJacksonValue;
    }

    @Transactional
    public ResponseEntity<String> activateCustomer(Long id) {
        if (adminActivationValidations.ActivationValidationsCustomer(id)) {
            Customer customer = customerRepository.findByCustomerUserId(id).get();
            userRepository.updateByIsActive(customer.getEmail(), true);

            emailSenderService.sendEmail(customer.getEmail(),
                    messageSource.getMessage("message35.txt", l, LocaleContextHolder.getLocale()), customer.getFirstName() + customer.getLastName() +
                            messageSource.getMessage("message36.txt", l, LocaleContextHolder.getLocale()));

            return ResponseEntity.ok(messageSource.getMessage("message7.txt", l, LocaleContextHolder.getLocale()));
        }
        return ResponseEntity.ok(messageSource.getMessage("message8.txt", l, LocaleContextHolder.getLocale()));
    }

    @Transactional
    public ResponseEntity<String> deactivateCustomer(String username, Long id) {

        UserEntity admin = userRepository.findByUsername(username).get();
        if (admin.getUserId().equals(id)) {
            return ResponseEntity.ok(messageSource.getMessage("message116.txt", l, LocaleContextHolder.getLocale()));
        }

        if (adminActivationValidations.DeactivationValidationsCustomer(id)) {
            Customer customer = customerRepository.findByCustomerUserId(id).get();
            userRepository.updateByIsActive(customer.getEmail(), false);

            emailSenderService.sendEmail(customer.getEmail(), messageSource.getMessage("message37.txt", l, LocaleContextHolder.getLocale()), "Dear " + customer.getFirstName() + " " + customer.getLastName() + messageSource.getMessage("message38.txt", l, LocaleContextHolder.getLocale()));
            return ResponseEntity.ok(messageSource.getMessage("message9.txt", l, LocaleContextHolder.getLocale()));
        }
        return ResponseEntity.ok(messageSource.getMessage("message10.txt", l, LocaleContextHolder.getLocale()));
    }

    @Transactional
    public ResponseEntity<String> activateSeller(Long id) {
        if (adminActivationValidations.ActivationValidationsSeller(id)) {
            Seller seller = sellerRepository.findById(id).get();
            userRepository.updateByIsActive(seller.getEmail(), true);

            emailSenderService.sendEmail(seller.getEmail(), messageSource.getMessage("message35.txt", l, LocaleContextHolder.getLocale()), seller.getFirstName() + seller.getLastName() + messageSource.getMessage("message36.txt", l, LocaleContextHolder.getLocale()));
            return ResponseEntity.ok(messageSource.getMessage("message7.txt", l, LocaleContextHolder.getLocale()));
        }
        return ResponseEntity.ok(messageSource.getMessage("message8.txt", l, LocaleContextHolder.getLocale()));
    }

    @Transactional
    public ResponseEntity<String> deactivateSeller(Long id) {
        if (adminActivationValidations.DeactivationValidationsSeller(id)) {
            Seller seller = sellerRepository.findById(id).get();
            userRepository.updateByIsActive(seller.getEmail(), false);

            emailSenderService.sendEmail(seller.getEmail(), messageSource.getMessage("message37.txt", l, LocaleContextHolder.getLocale()), "Dear " + seller.getFirstName() + " " + seller.getLastName() + messageSource.getMessage("message38.txt", l, LocaleContextHolder.getLocale()));
            return ResponseEntity.ok(messageSource.getMessage("message9.txt", l, LocaleContextHolder.getLocale()));
        }
        return ResponseEntity.ok(messageSource.getMessage("message10.txt", l, LocaleContextHolder.getLocale()));
    }

    public ResponseEntity<String> lockUser(Long user_id) {
        UserEntity user = null;
        Optional<UserEntity> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (user.getIsLocked())
                return ResponseEntity.ok().body(messageSource.getMessage("message2.txt", l, LocaleContextHolder.getLocale()));
            else {
                user.setIsLocked(true);
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(), messageSource.getMessage("message39.txt", l, LocaleContextHolder.getLocale()), "Dear " + user.getFirstName() + " " + user.getLastName() + " " + messageSource.getMessage("message11.txt", l, LocaleContextHolder.getLocale()));
                return ResponseEntity.ok().body(messageSource.getMessage("message5.txt", l, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new UserNotFoundException(messageSource.getMessage("message3.txt", l, LocaleContextHolder.getLocale()));
        }
    }

    public ResponseEntity<String> unlockUser(Long user_id) {
        UserEntity user = null;
        Optional<UserEntity> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (!user.getIsLocked()) {
                return ResponseEntity.ok().body(messageSource.getMessage("message1.txt", l, LocaleContextHolder.getLocale()));
            } else {
                user.setIsLocked(false);
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(), messageSource.getMessage("message40.txt", l, LocaleContextHolder.getLocale()), "Dear " + user.getFirstName() + " " + user.getLastName() + " " + messageSource.getMessage("message12.txt", l, LocaleContextHolder.getLocale()));
                return ResponseEntity.ok().body(messageSource.getMessage("message4.txt", l, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new UserNotFoundException(messageSource.getMessage("message3.txt", l, LocaleContextHolder.getLocale()));
        }
    }
}

