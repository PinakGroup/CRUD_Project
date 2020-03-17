package com.example.demo.controller;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.BlogUser;
import com.example.demo.service.JwtUtil;
import com.example.demo.service.LoginUserDetailsService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginUserDetailsService loginUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    
    @RequestMapping(method = RequestMethod.POST, path = "/authenticate", consumes = {"application/json"})
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
            );
        }
        catch(BadCredentialsException e){
            throw new Exception("Unauthenticated User",e);
        }

        final String jwtToken = jwtUtil.generateToken(loginUserDetailsService.loadUserByUsername(authenticationRequest.getUsername()));
        return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwtToken), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register", consumes = {"application/json"})
    public ResponseEntity<BlogUser> registerUser(@RequestBody BlogUser blogUser) {
        if(userService.checkUser(blogUser)){
            return new ResponseEntity<BlogUser>(new BlogUser(),HttpStatus.CONFLICT);
        }
        else{
            return new ResponseEntity<BlogUser>(userService.createUser(blogUser),HttpStatus.CREATED);
        }
    }
}
