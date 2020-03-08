package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    BlogUser createUser(BlogUser user){
        return userRepository.save(user);
    }

    boolean checkUser(BlogUser user){
        return userRepository.findById(user.getUsername()).isPresent();
    }

    boolean checkUserByName(String username){
        return userRepository.findById(username).isPresent();
    }

    BlogUser getUserByName(String username){
        return userRepository.findById(username).get();
    }
}
