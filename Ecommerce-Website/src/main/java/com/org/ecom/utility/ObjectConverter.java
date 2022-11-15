package com.org.ecom.utility;

import com.org.ecom.constant.APPConstant;
import com.org.ecom.dto.SellerDto;
import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.Seller;
import com.org.ecom.entities.User;
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
}
