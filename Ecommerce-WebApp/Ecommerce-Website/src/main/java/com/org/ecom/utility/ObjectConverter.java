package com.org.ecom.utility;

import com.org.ecom.constant.APPConstant;
import com.org.ecom.dto.CustomerDto;
import com.org.ecom.dto.SellerDto;
import com.org.ecom.entities.Customer;
import com.org.ecom.entities.Seller;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectConverter {

    @Autowired
    ModelMapper modelMapper;

    public Seller dtoToEntity(SellerDto sellerDto) {
        Seller seller = modelMapper.map(sellerDto, Seller.class);
        seller.setIsActive(!APPConstant.IS_ACTIVE);
        seller.setIsExpired(APPConstant.IS_EXPIRED);
        seller.setIsDeleted(APPConstant.IS_DELETED);
        seller.setIsLocked(APPConstant.IS_LOCKED);
        seller.setInvalidAttemptCount(APPConstant.INVALID_ATTEMPT_COUNT);
        return seller;
    }

    public SellerDto entityToDto(Seller seller) {
        SellerDto sellerDto = modelMapper.map(seller, SellerDto.class);
        sellerDto.setIsActive(seller.getIsActive());
        sellerDto.setIsExpired(seller.getIsExpired());
        sellerDto.setIsDeleted(seller.getIsDeleted());
        sellerDto.setIsLocked(seller.getIsLocked());
        sellerDto.setInvalidAttemptCount(seller.getInvalidAttemptCount());
        return sellerDto;
    }

    public Customer dtoToEntity(CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        customer.setIsActive(!APPConstant.IS_ACTIVE);
        customer.setIsExpired(APPConstant.IS_EXPIRED);
        customer.setIsDeleted(APPConstant.IS_DELETED);
        customer.setIsLocked(APPConstant.IS_LOCKED);
        customer.setInvalidAttemptCount(APPConstant.INVALID_ATTEMPT_COUNT);
        return customer;
    }

    public CustomerDto entityToDto(Customer customer) {
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        customerDto.setIsActive(customer.getIsActive());
        customerDto.setIsExpired(customer.getIsExpired());
        customerDto.setIsDeleted(customer.getIsDeleted());
        customerDto.setIsLocked(customer.getIsLocked());
        customerDto.setInvalidAttemptCount(customer.getInvalidAttemptCount());
        return customerDto;
    }
}
