package com.example.demo.controller;

import com.example.demo.model.Story;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@CrossOrigin
@RestController
public class StoryController {
    @Autowired
    private StoryService storyService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping(path = "/story", produces = {"application/json","application/xml"})
    ResponseEntity<Iterable<Story>> getStories(@RequestParam("num") int pageNumber, @RequestParam("size") int pageSize){
        return new ResponseEntity<>(storyService.getAllStories(pageNumber, pageSize), HttpStatus.OK);
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

    @PostMapping(value = "/uploadFile", consumes = {"application/json","application/xml","multipart/form-data"})
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){

        String fileName = fileStorageService.storeFile(file);
        return ResponseEntity.ok().body(fileName);
    }

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping(path = "/story", consumes = {"application/json","application/xml","multipart/form-data"}, produces = {"application/json","application/xml"})
    ResponseEntity<Story> createStory(@RequestPart("data") Story story, @RequestPart("file") MultipartFile file, Authentication authentication) throws Exception{
            HttpHeaders headers = new HttpHeaders();
            //headers.set("Content-Type","application/json");
            Story createdStory = storyService.createStory(story,(UserDetails)authentication.getPrincipal());
            String fileName = fileStorageService.storeFileWithOffset(file, createdStory.getId().toString());
            createdStory.setFileName(fileName);
            storyService.updateStory(createdStory, (UserDetails)authentication.getPrincipal());

            return new ResponseEntity<>(createdStory, headers, HttpStatus.OK);
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