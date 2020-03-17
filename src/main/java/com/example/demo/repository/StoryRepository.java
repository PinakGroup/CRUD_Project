package com.example.demo.repository;

import com.example.demo.model.Story;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends CrudRepository<Story,Long> {
    public List<Story> findAllByTitle(String title);
    public List<Story> findAll(Pageable pageable);
}
