package com.org.ecom.repository;

import com.org.ecom.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Integer> {

}
