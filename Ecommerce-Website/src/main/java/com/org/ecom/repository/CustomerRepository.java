package com.org.ecom.repository;

import com.org.ecom.entities.Customer;
import com.org.ecom.entities.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findById(Long id);

    Customer findByUsername(String username);

    Customer findByEmail(String email);

    List<Customer> findAll();

    @Query(value = "select c.ID from CUSTOMER c", nativeQuery = true)
    List<Long> findAllCustomerId();

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
}
