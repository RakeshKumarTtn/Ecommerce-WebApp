package com.org.ecom.service;

import com.org.ecom.exception.PatternMismatchException;
import com.org.ecom.repository.AddressRepository;
import com.org.ecom.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.org.ecom.dto.SellerDto;
import com.org.ecom.dto.SellerProfileDto;
import com.org.ecom.entities.Seller;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.utility.ObjectConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SellerService {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    ObjectConverter objectConverter;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CurrentUserService currentUserService;

    public Seller convtToSeller(SellerDto sellerDto) {
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        System.out.println("dto to seller object");
        return seller;
    }

    public SellerProfileDto toSellerViewProfileDto(Seller seller) {
        SellerProfileDto sellerProfileDto = modelMapper.map(seller, SellerProfileDto.class);
        return sellerProfileDto;
    }

    public SellerProfileDto viewProfile() {
        String username = currentUserService.getUser();
        Seller seller = sellerRepository.findByUsername(username);
        return toSellerViewProfileDto(seller);
    }

    public ResponseEntity updateProfile(SellerDto sellerDto) {
        String username = currentUserService.getUser();
        Seller seller = sellerRepository.findByEmail(username);
        if (sellerDto.getFirstName() != null)
            seller.setFirstName(sellerDto.getFirstName());
        if (sellerDto.getMiddleName() != null)
            seller.setMiddleName(sellerDto.getMiddleName());
        if (sellerDto.getLastName() != null)
            seller.setLastName(sellerDto.getLastName());
        if (sellerDto.getCompanyContact() != null) {
            if (sellerDto.getCompanyContact().toString().matches("(\\+91|0)[0-9]{10}")) {
                seller.setCompanyContact(sellerDto.getCompanyContact());
            } else {
                throw new PatternMismatchException("Contact number should start with +91 or 0 and length should be 10");
            }
        }
        if (sellerDto.getCompanyName() != null)
            seller.setCompanyName(sellerDto.getCompanyName());
        if (sellerDto.getGstin() != null)
            seller.setGstin(sellerDto.getGstin());
        sellerRepository.save(seller);
        return ResponseEntity.ok().body("profile is updated successfully");
    }

    public Set<Seller> getAllActiveSeller() {
        List<Seller> sellers = sellerRepository.findAll();
        Set<Seller> sellerSet = new HashSet<>();
        sellerSet = sellerRepository.listOfActiveUser("Something", true);
        return sellerSet;
    }

    @Transactional
    public Seller deleteSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        sellerRepository.deleteByEmail(!seller.getIsActive(), !seller.getIsDeleted(), email);
        return seller;
    }

    @Transactional
    public Seller deleteSellerByUserName(String username) {
        Seller seller = sellerRepository.findByUsername(username);
        if (seller.getIsActive())
            sellerRepository.deleteSellerByUserName(!seller.getIsActive(), !seller.getIsDeleted(), username);
        return seller;
    }

    @Transactional
    @Modifying
    public SellerDto updateSeller(SellerDto sellerDto, String email) {
        Seller seller = sellerRepository.findByEmail(email);
        sellerRepository.delete(seller);
        return objectConverter.entityToDto(sellerRepository.save(objectConverter.dtoToEntity(sellerDto)));
    }
}