package com.org.ecom.utility;

import com.org.ecom.entities.Customer;
import com.org.ecom.entities.UserEntity;

public interface Criteria {
    UserEntity findById(Long id);

    Customer findByEmail(String email);
}
