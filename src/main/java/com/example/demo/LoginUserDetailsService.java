package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        if(userService.checkUserByName(userName)){
            BlogUser blogUser = userService.getUserByName(userName);
            return new User(blogUser.getUsername(), blogUser.getPassword(), new ArrayList<>());
        }
        else{
            throw new UsernameNotFoundException("NOT FOUND");
        }
    }



}
