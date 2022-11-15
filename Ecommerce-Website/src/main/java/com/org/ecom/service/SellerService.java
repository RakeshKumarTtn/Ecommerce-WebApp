package com.org.ecom.service;

import com.org.ecom.dto.SellerDto;
import com.org.ecom.entities.Seller;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.utility.ObjectConverter;
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

    public SellerDto addSeller(SellerDto sellerDto) {
        Seller seller = objectConverter.dtoToEntity(sellerDto);
        seller = sellerRepository.save(seller);
        return objectConverter.entityToDto(seller);
    }

    public Set<Seller> getAllActiveSeller() {
        List<Seller> sellers = sellerRepository.findAll();
        Set<Seller> sellerSet = new HashSet<>();
        List<Long> sellerIds = sellerRepository.findAllSellerId();
        sellerSet = sellerRepository.listOfActiveUser("Something", true, sellerIds);
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
