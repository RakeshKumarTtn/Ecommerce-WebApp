package com.org.ecom.repository.registration;

import com.org.ecom.entity.registration.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndEmail(String username, String email);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndPassword(String email, String password);

    @Modifying
    @Query(value = "update USER set PASSWORD= :password where EMAIL= :email", nativeQuery = true)
    Integer updateUserPassword(@Param("password") String password, @Param("email") String email);

    @Modifying
    @Query(value = "update USER set IS_ACTIVE= :value where EMAIL= :email", nativeQuery = true)
    Integer updateByIsActive(@Param("email") String email, @Param("value") Boolean value);


    @Modifying
    @Query(value = "update USER set INVALID_ATTEMPT_COUNT= :invalidCount where USER_NAME= :username", nativeQuery = true)
    void updateInvalidAttemptCount(@Param("username") String username, @Param("invalidCount") int invalidCount);


    @Modifying
    @Query(value = "update USER set IS_EXPIRED= :value where EMAIL= :email", nativeQuery = true)
    Integer updateByIsExpired(@Param("email") String email, @Param("value") Boolean value);
}
