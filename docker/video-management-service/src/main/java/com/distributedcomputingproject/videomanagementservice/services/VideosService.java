package com.distributedcomputingproject.videomanagementservice.services;

import com.distributedcomputingproject.videomanagementservice.Status;
import com.distributedcomputingproject.videomanagementservice.entities.User;
import com.distributedcomputingproject.videomanagementservice.entities.Video;
import com.distributedcomputingproject.videomanagementservice.exceptions.*;
import com.distributedcomputingproject.videomanagementservice.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@Transactional
public class VideosService {

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    RestClientService restClientService;

    public Video addVideo(Video video, User user){
        if(videoRepository.existsByName(video.getName()))
            throw new VideoAlreadyExistsException(video.getName());
        video.setUser(user);
        video.setStatus(Status.NOT_UPLOADED);
        return videoRepository.save(video);
    }

    public Video getVideoById(Integer id){
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException(id));
        if(!video.isUploaded())
            throw new VideoNotFoundException(id);
        return video;
    }

    public Iterable<Video> getAllVideos(){
        return videoRepository.findAll();
    }

    public Video uploadVideo(Integer id, MultipartFile file, User user){
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException(id));
        if(!video.hasUser(user))
            throw new VideoUploadingNotAllowedException(id);
        if(video.isUploaded())
            throw new VideoJustUploadedException(id);
        try {
            Path path = Paths.get("/storage/var/video/" + video.getId() +"/video.mp4");
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved with success");
        }catch(Exception e){
            e.printStackTrace();
        }
        HttpStatus httpStatus = restClientService.sendPostRequestWithJson("http://video-processing-service_1:5000/videos/process/","{\"videoId\":\""+ video.getId() +"\"}");
        if(httpStatus == HttpStatus.OK){
            System.out.println("File uploaded with success");
            video.setStatus(Status.UPLOADED);
            return videoRepository.save(video);
        }
        else
            throw new VideoUploadingErrorException(id);
    }
}
