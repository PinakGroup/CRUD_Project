package com.example.demo.configure;

import com.example.demo.service.JwtUtil;
import com.example.demo.service.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestFilter extends OncePerRequestFilter {

    @Autowired
    private LoginUserDetailsService loginUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        boolean isValidToken = false;
        String jwtToken = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            jwtToken = authorizationHeader.substring(7);
            if(jwtUtil.extractUsername(jwtToken)!=null){
                try {
                    UserDetails userDetails = loginUserDetailsService.loadUserByUsername(jwtUtil.extractUsername(jwtToken));
                    isValidToken = jwtUtil.validateToken(jwtToken, userDetails);
                }
                catch(UsernameNotFoundException e){
                    isValidToken = false;
                }
            }
        }

        if(isValidToken){
            UserDetails userDetails = loginUserDetailsService.loadUserByUsername(jwtUtil.extractUsername(jwtToken));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }
}
