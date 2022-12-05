package com.org.ecom.repository.security;


import com.org.ecom.entity.security.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResetTokenRepo extends JpaRepository<ResetToken, Long> {

    Optional<ResetToken> findByToken(String token);

    @Modifying
    @Query(value = "delete from RESET_TOKEN where user_id= :user_id && Token= :token", nativeQuery = true)
    void delete(@Param("user_id") Long user_id, @Param("token") String token);
}

