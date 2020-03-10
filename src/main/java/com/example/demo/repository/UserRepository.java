package com.example.demo.repository;

import com.example.demo.model.BlogUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<BlogUser,String> {

}
