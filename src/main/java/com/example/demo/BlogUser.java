package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class BlogUser {

    @NotNull
    @Id
    private String username;
    @NotNull
    @JsonIgnore
    private String password;

    public BlogUser() {
    }

    public BlogUser(String username,String password){
        this.username = username;
        this.password = password;
    }

    public BlogUser(UserDetails userDetails){
        this.username = userDetails.getUsername();
        this.password = userDetails.getPassword();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
