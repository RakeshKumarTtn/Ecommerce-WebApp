package com.org.ecom.repository.registration;

import com.org.ecom.entity.registration.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findById(Long id);

    Optional<Customer> findByUsername(String username);


    Boolean existsByEmail(String email);

    Page<Customer> findByEmail(String email, PageRequest pageable);

    Optional<Customer> findByEmail(String email);

    Page<Customer> findAll(Pageable pageable);


    @Query(value = "select * from USER u where u.EMAIL!= :email and u.IS_ACTIVE= :isActive and u.ID in(:cusIds)", nativeQuery = true)
    Set<Customer> listOfActiveUser(@Param("email") String email, @Param("isActive") Boolean isActive, @Param("cusIds") List<Long> cusIds);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive,u.IS_DELETED =:isDeleted  WHERE u.EMAIL = :email", nativeQuery = true)
    void deleteByEmail(@Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted, @Param("email") String email);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive,u.IS_DELETED =:isDeleted WHERE u.USER_NAME = :username", nativeQuery = true)
    void deleteSellerByUserName(@Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted, @Param("username") String username);

    @Query(value = "select * from USER u where u.ID =:id", nativeQuery = true)
    Optional<Customer> findByCustomerUserId(@Param("id") Long id);
}
