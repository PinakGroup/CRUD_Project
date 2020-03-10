package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoryController {
    @Autowired
    public StoryService storyService;

    @GetMapping(path = "/story", produces = {"application/json","application/xml"})
    ResponseEntity<List<Story>> getStories(){
        return new ResponseEntity<>(storyService.getAllStories(), HttpStatus.OK);
    }

    @GetMapping(path = "/story/{id}",produces = {"application/json","application/xml"})
    ResponseEntity<Story> getStoryById(@PathVariable Long id){
        if(storyService.getStoryById(id).isPresent()){
            return new ResponseEntity<>(storyService.getStoryById(id).get(), HttpStatus.OK);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/story", consumes = {"application/json","application/xml"}, produces = {"application/json","application/xml"})
    ResponseEntity<Story> createStory(@RequestBody Story story, Authentication authentication){
            HttpHeaders headers = new HttpHeaders();
            //headers.set("Content-Type","application/json");

            return new ResponseEntity<>(storyService.createStory(story,(UserDetails)authentication.getPrincipal()), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/story/{id}")
    ResponseEntity<Story> deleteStoryById(@PathVariable Long id, Authentication authentication) throws Exception{
        return storyService.deleteStoryById(id,(UserDetails)authentication.getPrincipal())?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/story")
    ResponseEntity<Story> updateStory(@RequestBody Story story, Authentication authentication) throws Exception{
        return storyService.updateStory(story,(UserDetails)authentication.getPrincipal())?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }


}