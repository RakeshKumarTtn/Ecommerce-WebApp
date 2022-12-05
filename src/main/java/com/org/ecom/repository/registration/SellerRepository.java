package com.org.ecom.repository.registration;

import com.org.ecom.entity.registration.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@EnableJpaRepositories
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findById(Long id);

    Optional<Seller> findByUsername(String username);

    Page<Seller> findByEmail(String email, PageRequest pageable);

    Optional<Seller> findByEmail(String email);

    List<Seller> findAll();

    @Query(value = "select * from USER u where u.EMAIL!= :email and u.IS_ACTIVE= :isActive", nativeQuery = true)
    Set<Seller> listOfActiveUser(@Param("email") String email, @Param("isActive") Boolean isActive);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive,u.IS_DELETED =:isDeleted  WHERE u.EMAIL = :email", nativeQuery = true)
    void deleteByEmail(@Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted, @Param("email") String email);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive,u.IS_DELETED =:isDeleted WHERE u.USER_NAME = :username", nativeQuery = true)
    void deleteSellerByUserName(@Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted, @Param("username") String username);

    Boolean existsByEmail(String email);
}
