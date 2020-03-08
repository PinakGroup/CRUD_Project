package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {
    @Autowired
    public StoryRepo storyRepository;

    List<Story> getAllStories(){
        List<Story> ret = new ArrayList<Story>();
        storyRepository.findAll().forEach(ret::add);
        return ret;
    }

    List<Story> getStoriesByTitle(String title){
        return storyRepository.findAllByTitle(title);
    }

    Optional<Story> getStoryById(Long id){
        return storyRepository.findById(id);
    }

    Story createStory(Story story){
        return storyRepository.save(story);
    }

    boolean deleteStoryById(Long id){
        if(storyRepository.findById(id).isPresent()){
            storyRepository.deleteById(id);
            return true;
        }
        else return false;
    }

    boolean updateStory(Story story){
        if(storyRepository.findById(story.getId()).isPresent()){
            storyRepository.save(story);
            return true;
        }
        else return false;
    }
}
