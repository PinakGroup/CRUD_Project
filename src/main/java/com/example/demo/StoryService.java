package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoryService {
    @Autowired
    public StoryRepository storyRepository;

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

    Story createStory(Story story, UserDetails userDetails){
        System.out.println(userDetails.getUsername());
        story.setBlogUser(new BlogUser(userDetails));
        return storyRepository.save(story);
    }

    boolean deleteStoryById(Long id,UserDetails userDetails) throws Exception{

        Optional<Story> story = storyRepository.findById(id);

        if(story.isPresent()){
            if(!story.get().getBlogUser().getUsername().equals(userDetails.getUsername())){
                throw new AccessDeniedException("Forbidden");
            }
            storyRepository.deleteById(id);
            return true;
        }
        else return false;
    }
    boolean updateStory(Story storyForUpdate,UserDetails userDetails) throws Exception{

        Optional<Story> story = storyRepository.findById(storyForUpdate.getId());

        if(story.isPresent()){
            if(!story.get().getBlogUser().getUsername().equals(userDetails.getUsername())){
                throw new AccessDeniedException("Forbidden");
            }

            storyForUpdate.setBlogUser(story.get().getBlogUser());
            storyRepository.save(storyForUpdate);
            return true;
        }
        else return false;
    }

}
