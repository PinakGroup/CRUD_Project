package com.example.demo.service;
import com.example.demo.model.BlogUser;
import com.example.demo.model.Story;
import com.example.demo.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Story> getAllStories(int pageNumber, int pageSize){
        List<Story> ret = new ArrayList<Story>();
        Pageable pageable = PageRequest.of(pageNumber,pageSize, Sort.by("date").descending());
        //storyRepository.findAll(pageable).forEach(ret::add);
        return storyRepository.findAll(pageable);
    }

    public List<Story> getStoriesByTitle(String title){
        return storyRepository.findAllByTitle(title);
    }

    public Optional<Story> getStoryById(Long id){
        return storyRepository.findById(id);
    }

    public Story createStory(Story story, UserDetails userDetails){
        story.setBlogUser(new BlogUser(userDetails));
        return storyRepository.save(story);
    }

    public boolean deleteStoryById(Long id,UserDetails userDetails) throws Exception{

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

    public boolean updateStory(Story storyForUpdate,UserDetails userDetails) throws Exception{

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
