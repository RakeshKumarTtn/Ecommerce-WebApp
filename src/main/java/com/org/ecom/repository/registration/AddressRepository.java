package com.org.ecom.repository.registration;

import com.org.ecom.entity.registration.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByAddressId(Integer addressId);
}
