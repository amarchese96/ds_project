package com.distributedcomputingproject.videomanagementservice.services;

import com.distributedcomputingproject.videomanagementservice.entities.User;
import com.distributedcomputingproject.videomanagementservice.exceptions.UserNotFoundException;
import com.distributedcomputingproject.videomanagementservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                getEncoder().encode(user.getPassword()),
                true,
                true,
                true,
                true,
                Collections.emptyList()
        );
    }

    @Bean
    private BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
