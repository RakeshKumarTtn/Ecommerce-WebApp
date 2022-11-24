package com.org.ecom.service;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.org.ecom.dto.AddressDto;
import com.org.ecom.dto.RegisteredCustomerDto;
import com.org.ecom.dto.RegisteredSellerDto;
import com.org.ecom.entities.Address;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.exception.UserNotFoundException;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    ModelMapper modelMapper;

    public List<RegisteredCustomerDto> getAllRegisteredCustomers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Order.asc(sortBy)));

        List<Long> allCustomersList = userRepository.findIdOfCustomers(paging);
        List<RegisteredCustomerDto> list = new ArrayList<>();
        for (Long userId : allCustomersList) {
            Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
            UserEntity user = optionalUserEntity.get();
            if (user.getUserId() == userId) {
                RegisteredCustomerDto registeredCustomersDTO = modelMapper.map(user, RegisteredCustomerDto.class);
                list.add(registeredCustomersDTO);
            }
        }
        return list;
    }

    public List<RegisteredSellerDto> getAllRegisteredSellers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Order.asc(sortBy)));

        List<RegisteredSellerDto> list = new ArrayList<>();
        for (Long userId : userRepository.findIdOfSellers(paging)) {
            Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
            UserEntity user = optionalUserEntity.get();
            if (user.getUserId() == userId) {
                RegisteredSellerDto registeredSellersDTO = modelMapper.map(user, RegisteredSellerDto.class);
                AddressDto addressDTO = new AddressDto();
                for (Address address : user.getUserAddress()) {
                    addressDTO = modelMapper.map(address, AddressDto.class);
                }
                registeredSellersDTO.setAddressDTO(addressDTO);
                list.add(registeredSellersDTO);
            }
        }
        return list;
    }

    public ResponseEntity lockUser(Long user_id) {
        UserEntity user = null;
        Long[] l = {};
        Optional<UserEntity> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (user.getIsLocked() == true)
                return ResponseEntity.ok().body(messageSource.getMessage("message26.txt", l, LocaleContextHolder.getLocale()));
            else {
                user.setIsLocked(true);
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(), "Regarding Account Locked", "Dear " + user.getFirstName() + " " + user.getLastName() + " " + messageSource.getMessage("message28.txt", l, LocaleContextHolder.getLocale()));
                return ResponseEntity.ok().body(messageSource.getMessage("message27.txt", l, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new UserNotFoundException(messageSource.getMessage("message3.txt", l, LocaleContextHolder.getLocale()));
        }
    }

    public ResponseEntity unlockUser(Long user_id) {
        UserEntity user = null;
        Long[] l = {};
        Optional<UserEntity> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (user.getIsLocked() == false) {
                return ResponseEntity.ok().body(messageSource.getMessage("message24.txt", l, LocaleContextHolder.getLocale()));
            } else {
                user.setIsLocked(false);
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(), "regarding account", "Dear " + user.getFirstName() + " " + user.getLastName() + " " + messageSource.getMessage("message29.txt", l, LocaleContextHolder.getLocale()));
                return ResponseEntity.ok().body(messageSource.getMessage("message25.txt", l, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new UserNotFoundException(messageSource.getMessage("message3.txt", l, LocaleContextHolder.getLocale()));
        }
    }
}