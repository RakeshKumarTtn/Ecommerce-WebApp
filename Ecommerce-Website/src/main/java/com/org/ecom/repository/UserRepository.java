package com.org.ecom.repository;

import com.org.ecom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    @Query(value = "select * from USER u where u.EMAIL!= :email and u.IS_ACTIVE= :isActive", nativeQuery = true)
    Set<User> listOfActiveUser(@Param("email") String email, @Param("isActive") Boolean isActive);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive WHERE u.EMAIL = :email", nativeQuery = true)
    void deleteByEmail(@Param("isActive") Boolean isActive, @Param("email") String email);

    //Modifying queries only used with void int return type
    @Modifying
    @Query(value = "update USER u SET u.IS_ACTIVE = :isActive WHERE u.USER_NAME = :username", nativeQuery = true)
    void deleteUserByUserName(@Param("isActive") Boolean isActive, @Param("username") String username);
}
