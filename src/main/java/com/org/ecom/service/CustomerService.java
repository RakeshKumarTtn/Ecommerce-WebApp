package com.org.ecom.service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.org.ecom.dto.login.AddressDto;
import com.org.ecom.dto.login.CustomerDto;
import com.org.ecom.entity.registration.Address;
import com.org.ecom.entity.registration.Customer;
import com.org.ecom.entity.registration.Role;
import com.org.ecom.entity.security.ActivationToken;
import com.org.ecom.exception.UserNotFoundException;
import com.org.ecom.repository.registration.AddressRepository;
import com.org.ecom.repository.registration.CustomerRepository;
import com.org.ecom.repository.registration.RoleRepository;
import com.org.ecom.repository.registration.UserRepository;
import com.org.ecom.security.CustomizedAuditorAware;
import com.org.ecom.utility.CustomJwtUtility;
import com.org.ecom.utility.ObjectConverter;
import com.org.ecom.validation.RegisterValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.org.ecom.enums.RoleLevel.CUSTOMER;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    private CustomizedAuditorAware customizedAuditorAware;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private RegisterValidations registerValidations;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ActivationTokenService activationTokenService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private CustomJwtUtility customJwtUtility;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    MessageSource messageSource;

    @Autowired
    EntityManager entityManager;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    public String path;

    /*
        Method for registration of Customer while registration check the below conditions:
            Check password and confirm password is matched or not.
            Giving Customer role to the Customer
            Sending the Mail to the Customer
    */
    public void registerCustomer(CustomerDto customerDto) {

        if (registerValidations.passwordAndConfirmPasswordMatchCustomer(customerDto)) {
            customizedAuditorAware.setName(customerDto.getFirstName());

            Customer customer = objectConverter.dtoToEntity(customerDto);

            Optional<Role> byAuthority = roleRepository.findByAuthority(String.valueOf(CUSTOMER));

            byAuthority.ifPresent(customer::addRole);

            for (AddressDto addressDto : customerDto.getAddressDtoSet()) {
                Address address = objectConverter.dtoToEntity(addressDto);
                customer.addAddress(address);
            }

            customer.setIsExpired(false);

            customer.setPassword(passwordEncoder.encode(customer.getPassword()));

            userRepository.save(customer);

            String resetToken = jwtUtility.generateResetToken(customer);

            ActivationToken activationToken = new ActivationToken();
            activationToken.setToken(resetToken);
            activationToken.setUser(customer);
            activationTokenService.saveData(activationToken);

            emailSenderService.sendEmail(customer.getEmail(),
                    "Regarding Account activation.",
                    messageSource.getMessage("message41.txt", null, LocaleContextHolder.getLocale()) + resetToken);
        }
    }

    /*
        Method for fetching the Customer according to the ID
    */
    public Optional<Customer> findByUserId(Long id) {
        return customerRepository.findById(id);
    }

    /*
        Method for fetching the Customer Information from database
        Token is passed and used for getting the Customer Information from the database
    */
    public MappingJacksonValue customerData(String token) {
        String username = customJwtUtility.extractUsername(token);
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username"));

        Set<String> fileList = fileService.listFilesUsingDirectoryStream();
        customer.setImageFilePath(fileList.contains(username.concat(".png")) ? path + File.separator + username + ".png" : null);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(customer);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("userId",
                "firstName", "lastName", "isActive", "contact", "imageFilePath");

        FilterProvider filters = new SimpleFilterProvider().addFilter("CustomersFilter", filter);

        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

    //Method for getting the customer Addresses
    public MappingJacksonValue viewCustomerAddresses(String token) {

        String username = customJwtUtility.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByUsername(username);

        if (!customer.isPresent())
            throw new UserNotFoundException("Customer not found");

        List<Address> addressList = new ArrayList<>();
        for (Address address : customer.get().getUserAddress()) {
            addressList.add(address);
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("addresses");

        //creating filter using FilterProvider class
        FilterProvider filters = new SimpleFilterProvider().addFilter("CustomersFilter", filter);

        //constructor of MappingJacksonValue class  that has bean as constructor argument
        MappingJacksonValue mapping = new MappingJacksonValue(addressList);

        //configuring filters
        mapping.setFilters(filters);
        return mapping;
    }

    //Method for updating the customer profile
    public ResponseEntity<String> updateCustomerProfile(Customer customer, Customer request) {

        if (request.getFirstName() != null)
            customer.setFirstName(request.getFirstName());

        if (request.getMiddleName() != null)
            customer.setMiddleName(request.getMiddleName());

        if (request.getLastName() != null)
            customer.setLastName(request.getLastName());

        if (request.getEmail() != null)
            customer.setEmail(request.getEmail());

        if (request.getContact() != null)
            customer.setContact(request.getContact());

        customizedAuditorAware.setName(customer.getFirstName());
        customerRepository.save(customer);
        return ResponseEntity.ok(messageSource.getMessage("message42.txt", null, LocaleContextHolder.getLocale()));
    }

    //Method for updating the Customer Password
    public ResponseEntity<String> updatePassword(Long id, String password, String confirmPassword) {

        Optional<Customer> customerById = customerRepository.findById(id);

        if (customerById.isPresent()) {
            if (!password.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})"))
                return ResponseEntity.ok(messageSource.getMessage("message54.txt", null, LocaleContextHolder.getLocale()));
            if (!confirmPassword.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})"))
                return ResponseEntity.ok(messageSource.getMessage("message55.txt", null, LocaleContextHolder.getLocale()));

            Customer customer = customerById.get();
            String oldPassword = customer.getPassword();
            String newPassword = passwordEncoder.encode(password);
            if (password.equals(confirmPassword)) {
                if (!oldPassword.equals(newPassword)) {
                    customer.setPassword(newPassword);
                    customizedAuditorAware.setName(customer.getFirstName());
                    customerRepository.save(customer);

                    emailSenderService.sendEmail(customer.getEmail(), messageSource.getMessage("message60.txt", null, LocaleContextHolder.getLocale()), messageSource.getMessage("message61.txt", null, LocaleContextHolder.getLocale()));
                    return ResponseEntity.ok(messageSource.getMessage("message43.txt", null, LocaleContextHolder.getLocale()));
                } else
                    return ResponseEntity.ok(messageSource.getMessage("message44.txt", null, LocaleContextHolder.getLocale()));
            } else
                return ResponseEntity.ok(messageSource.getMessage("message45.txt", null, LocaleContextHolder.getLocale()));
        } else
            throw new UserNotFoundException(messageSource.getMessage("message46.txt", null, LocaleContextHolder.getLocale()));
    }

    //Method for updating the Customer Addresses
    public ResponseEntity<String> updateAddress(Long id, Integer address_id, Address address) {
        Optional<Address> byId = addressRepository.findByAddressId(address_id);
        Customer customer = customerRepository.findById(id).get();
        Optional<Address> addressId = customer
                .getUserAddress()
                .stream()
                .filter(e -> e.getAddressId().equals(address_id))
                .findFirst();
        Integer dbAddressId = null;
        if (addressId.isPresent()) {
            dbAddressId = addressId.get().getAddressId();
        }

        if (byId.isPresent() && dbAddressId == address_id) {
            Address address1 = byId.get();
            if (address.getCityName() != null)
                address1.setCityName(address.getCityName());
            if (address.getAddressLine() != null)
                address1.setAddressLine(address.getAddressLine());
            if (address.getStateName() != null)
                address1.setStateName(address.getStateName());
            if (address.getCountryName() != null)
                address1.setCountryName(address.getCountryName());
            if (address.getZipCode() != null)
                address1.setZipCode(address.getZipCode());
            if (address.getLabel() != null)
                address1.setLabel(address.getLabel());

            addressRepository.save(address1);
            return ResponseEntity.ok(messageSource.getMessage("message33.txt", null, LocaleContextHolder.getLocale()));
        } else
            return ResponseEntity.ok(messageSource.getMessage("message34.txt", null, LocaleContextHolder.getLocale()));
    }

    //Method for add new address of Customers
    public ResponseEntity<String> addAddressForUser(Long id, Address address) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent())
            throw new UserNotFoundException(messageSource.getMessage("message48.txt", null, LocaleContextHolder.getLocale()) + id);
        else {
            Customer customer = optionalCustomer.get();
            address.setUser(customer);
            addressRepository.save(address);
            return ResponseEntity.ok(messageSource.getMessage("message47.txt", null, LocaleContextHolder.getLocale()));
        }
    }

    //Method for deleting an address of a Customer
    public ResponseEntity<String> deleteAddressForUser(Long id, Integer address_id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(messageSource.getMessage("message48.txt", null, LocaleContextHolder.getLocale()) + id));

        Optional<Address> address = addressRepository.findById(address_id);

        if (address.isPresent()) {
            Address address1 = address.get();

            Set<Address> add = customer.getUserAddress();
            if (!add.contains(addressRepository.findById(address_id).get())) {
                return ResponseEntity.ok(messageSource.getMessage("message117.txt", null, LocaleContextHolder.getLocale()));
            }
            add = add.stream().filter(p -> !Objects.equals(p.getAddressId(), address_id)).collect(Collectors.toSet());
            customer.setUserAddress(add);

            addressRepository.deleteById(address_id);
            customizedAuditorAware.setName(customer.getFirstName());
            userRepository.save(customer);

            return ResponseEntity.ok(messageSource.getMessage("message49.txt", null, LocaleContextHolder.getLocale()));
        } else
            return ResponseEntity.ok(messageSource.getMessage("message50.txt", null, LocaleContextHolder.getLocale()) + address_id);
    }
}
