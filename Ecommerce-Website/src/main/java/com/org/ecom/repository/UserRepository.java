package com.org.ecom.repository;

import com.org.ecom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    User findByFirstName(String firstName);

    User findByEmail(String email);

    List<User> findAll();

    @Query(value = "select * from USER u where u.EMAIL= :email and u.IS_ACTIVE= :isActive", nativeQuery = true)
    User listOfActiveUser(@Param("email") String email, @Param("isActive") Boolean isActive);
}
