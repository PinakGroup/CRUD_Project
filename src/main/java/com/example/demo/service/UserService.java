package com.example.demo.service;

import com.example.demo.model.BlogUser;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public BlogUser createUser(BlogUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean checkUser(BlogUser user){
        return userRepository.findById(user.getUsername()).isPresent();
    }

    public boolean checkUserByName(String username){
        return userRepository.findById(username).isPresent();
    }

    public BlogUser getUserByName(String username){
        return userRepository.findById(username).get();
    }
}