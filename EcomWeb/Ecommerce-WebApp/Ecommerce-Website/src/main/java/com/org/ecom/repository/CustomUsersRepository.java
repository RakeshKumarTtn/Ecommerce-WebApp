package com.org.ecom.repository;


import com.org.ecom.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface CustomUsersRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameAndIsActiveAndIsLockedAndIsExpired(String username, Boolean isActive, Boolean isLocked, Boolean isExpired);

    Optional<UserEntity> findByUsernameAndEmail(String username, String email);
}
