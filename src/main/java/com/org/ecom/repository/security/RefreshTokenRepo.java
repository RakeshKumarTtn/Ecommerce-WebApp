package com.org.ecom.repository.security;

import com.org.ecom.entity.security.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface RefreshTokenRepo extends JpaRepository<RefreshTokens, Long> {

    @Query(value = "select * from REFRESH_TOKEN where user_id= :user_id", nativeQuery = true)
    Optional<Set<RefreshTokens>> findByUserId(@Param("user_id") Long user_id);

    @Modifying
    @Query(value = "delete from REFRESH_TOKEN where expireDate>= :date", nativeQuery = true)
    void deleteExpiredTokens(@Param("date") Date date);

    @Query(value = "select * from REFRESH_TOKEN where Token= :token and user_id= :user_id", nativeQuery = true)
    Optional<RefreshTokens> findByToken(@Param("token") String token, @Param("user_id") Long user_id);
}
