package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
public class FrontController{
    @Autowired
    public BlogService blogService;

    @GetMapping(path = "/story", produces = {"application/json","application/xml"})
    ResponseEntity<List<Story>> getStories(){
        return new ResponseEntity<>(blogService.getAllStories(),HttpStatus.OK);
    }

    @GetMapping(path = "/story/{id}",produces = {"application/json","application/xml"})
    ResponseEntity<Story> getStoryById(@PathVariable Long id){
        if(blogService.getStoryById(id).isPresent()){
            return new ResponseEntity<>(blogService.getStoryById(id).get(),HttpStatus.OK);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/story", consumes = {"application/json","application/xml"}, produces = {"application/json","application/xml"})
    ResponseEntity<Story> createStory(@RequestBody Story story){
            HttpHeaders headers = new HttpHeaders();
            //headers.set("Content-Type","application/json");
            return new ResponseEntity<>(blogService.createStory(story), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/story/{id}")
    ResponseEntity<Story> deleteStoryById(@PathVariable Long id){
        return blogService.deleteStoryById(id)?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }


    @RequestMapping(method = RequestMethod.PUT,path = "/story")
    ResponseEntity<Story> updateStory(@RequestBody Story story){
        return blogService.updateStory(story)?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }


}