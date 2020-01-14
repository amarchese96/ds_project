package com.distributedcomputingproject.videomanagementservice.services;

import com.distributedcomputingproject.videomanagementservice.entities.User;
import com.distributedcomputingproject.videomanagementservice.exceptions.UserAlreadyExistsException;
import com.distributedcomputingproject.videomanagementservice.exceptions.UserNotFoundException;
import com.distributedcomputingproject.videomanagementservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsersService {

    @Autowired
    UserRepository userRepository;

    public User addUser(User user){
        if(userRepository.existsByEmail(user.getEmail()))
            throw new UserAlreadyExistsException(user.getEmail());
        return userRepository.save(user);
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

    }

}
