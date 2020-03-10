package com.example.demo.repository;

import com.example.demo.model.Story;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends CrudRepository<Story,Long> {

    public List<Story> findAllByTitle(String title);
}
