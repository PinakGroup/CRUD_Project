package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginUserDetailsService loginUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

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

}
