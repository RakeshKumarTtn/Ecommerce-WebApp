package com.org.ecom.repository;

import com.org.ecom.entities.Seller;
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

    Seller findByUsername(String username);

    Seller findByEmail(String email);

    List<Seller> findAll();

    @Query(value = "select c.ID from SELLER c", nativeQuery = true)
    List<Long> findAllSellerId();

    @Query(value = "select * from USER u where u.EMAIL!= :email and u.IS_ACTIVE= :isActive and u.ID in(:sellerIds)", nativeQuery = true)
    Set<Seller> listOfActiveUser(@Param("email") String email, @Param("isActive") Boolean isActive, @Param("sellerIds") List<Long> sellerIds);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive,u.IS_DELETED =:isDeleted  WHERE u.EMAIL = :email", nativeQuery = true)
    void deleteByEmail(@Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted, @Param("email") String email);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive,u.IS_DELETED =:isDeleted WHERE u.USER_NAME = :username", nativeQuery = true)
    void deleteSellerByUserName(@Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted, @Param("username") String username);

    /*@Modifying
    @Query(value = "update USER u SET u.USER_NAME =:username,u.FIRST_NAME =:firstname,u.LAST_NAME =:lastname, u.MIDDLE_NAME =:middlename" +
            ",u.EMAIL =:email, u.PASSWORD =:password,u.IS_ACTIVE =:isActive,u.IS_DELETED =:isDeleted,u.IS_LOCKED =:isLocked" +
            ",u.IS_EXPIRED =: isExpired,u.PASSWORD_UPDATE_DATE =:dt,u.INVALID_ATTEMPT_COUNT =:attempt,u.modifiedBy=:email,modified=:dt where u.EMAIL =:email", nativeQuery = true)
    void updateUser(@Param("username") String username, @Param("firstname") String firstname, @Param("lastname") String lastname,
                    @Param("middlename") String middlename, @Param("email") String email, @Param("password") String password,
                    @Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted, @Param("isLocked") Boolean isLocked,
                    @Param("isExpired") Boolean isExpired, @Param("dt") Date dt, @Param("attempt") Integer invalidAttemptCount);*/
}
