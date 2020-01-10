package com.distributedcomputingproject.videomanagementservice.controllers;

import com.distributedcomputingproject.videomanagementservice.entities.User;
import com.distributedcomputingproject.videomanagementservice.entities.Video;
import com.distributedcomputingproject.videomanagementservice.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    @Autowired
    UsersService usersService;

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return new ResponseEntity<User>(usersService.addUser(user), HttpStatus.OK);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<User> me(Authentication authentication) {
        return new ResponseEntity<User>(usersService.getByEmail(authentication.getName()), HttpStatus.OK);
    }

}
