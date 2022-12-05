package com.org.ecom.repository.security;

import com.org.ecom.entity.security.AccessTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface AccessTokensRepo extends JpaRepository<AccessTokens, Long> {

    @Query(value = "select * from ACCESS_TOKEN where user_id= :user_id", nativeQuery = true)
    Optional<Set<AccessTokens>> findByUserId(@Param("user_id") Long user_id);

    @Modifying
    @Query(value = "delete from ACCESS_TOKEN where expireDate>= :date", nativeQuery = true)
    void deleteExpiredTokens(@Param("date") Date date);

    @Modifying
    @Query(value = "delete from ACCESS_TOKEN where Token= :token", nativeQuery = true)
    void logoutDeleteToken(@Param("token") String token);

    @Query(value = "select * from ACCESS_TOKEN where Token= :token and user_id= :user_id", nativeQuery = true)
    Optional<AccessTokens> findByToken(@Param("token") String token, @Param("user_id") Long user_id);
}
