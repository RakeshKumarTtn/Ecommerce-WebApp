package com.org.ecom.repository;


import com.org.ecom.entities.BlackListedTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListedTokensRepo extends JpaRepository<BlackListedTokens,Long> {

    @Query(value = "select * from BlackListedTokens where jwtToken = :token",nativeQuery = true)
    Optional<BlackListedTokens> findByToken(@Param("token") String token);
}
