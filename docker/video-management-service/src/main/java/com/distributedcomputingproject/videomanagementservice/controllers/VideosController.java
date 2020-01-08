package com.distributedcomputingproject.videomanagementservice.controllers;

import com.distributedcomputingproject.videomanagementservice.entities.Video;
import com.distributedcomputingproject.videomanagementservice.services.UsersService;
import com.distributedcomputingproject.videomanagementservice.services.VideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping(path = "/videos")
public class VideosController {

    @Autowired
    VideosService videosService;

    @Autowired
    UsersService usersService;

    @PostMapping(path = "")
    public ResponseEntity<Video> addVideo(Authentication authentication, @RequestBody Video video){
        return new ResponseEntity<Video>(videosService.addVideo(video, usersService.getByEmail(authentication.getName())), HttpStatus.OK);
    }

    @GetMapping(path = "")
    public ResponseEntity<Iterable<Video>> getAllVideos(){
        return new ResponseEntity<Iterable<Video>>(videosService.getAllVideos(), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity getVideoById(Authentication authentication, @PathVariable Integer id){
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/videofiles/" + videosService.getVideoById(id).getId() + "/video.mpd"));
        return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping(path = "{id}")
    public ResponseEntity<Video> uploadVideo(Authentication authentication, @PathVariable Integer id, @RequestParam("file") MultipartFile file){
        return new ResponseEntity<Video>(videosService.uploadVideo(id, file,usersService.getByEmail(authentication.getName())), HttpStatus.OK);
    }



}
