package com.org.ecom.service;

import com.org.ecom.entities.Role;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.repository.CustomUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    CustomUsersRepository customUsersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity byUsername = customUsersRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Username not found"));


        return new User(byUsername.getUsername(), byUsername.getPassword(),
                mapRolesToAuthorities(byUsername.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> rolesSet){
        return rolesSet.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toSet());
    }

    public void save(UserEntity user){
        customUsersRepository.save(user);
    }
}