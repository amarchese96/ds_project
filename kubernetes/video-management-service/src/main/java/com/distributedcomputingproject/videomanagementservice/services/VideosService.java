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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class VideosService {

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    RestClientService restClientService;

    @Autowired
    KafkaProducerService kafkaProducerService;

    public Video addVideo(Video video, User user){
        if(videoRepository.existsByName(video.getName()))
            throw new VideoAlreadyExistsException(video.getName());
        video.setUser(user);
        video.setStatus(Status.WAITING_UPLOAD);
        return videoRepository.save(video);
    }

    public Video getVideoById(Integer id){
        Video video = videoRepository.findById(id).orElseThrow(() -> new VideoNotFoundException(id));
        if(!video.isAvailable())
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
        if(!video.isWaitingUpload())
            throw new VideoNotWaitingUploadException(id);
        try {
            Path path = Paths.get("/storage/var/video/" + video.getId() +"/video.mp4");
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved with success");
        }catch(Exception e){
            throw new VideoUploadingErrorException(id);
        }

        video.setStatus(Status.UPLOADED);
        kafkaProducerService.sendProcessMessage(video.getId());
        return videoRepository.save(video);
    }

    public void setVideoAvailable(Integer videoId){
       Optional<Video> optional = videoRepository.findById(videoId);
       if(optional.isPresent()){
           Video video = optional.get();
           if(video.isUploaded()) {
               video.setStatus(Status.AVAILABLE);
               videoRepository.save(video);
           }
       }
    }

    public void setVideoNotAvailable(Integer videoId){
        Optional<Video> optional = videoRepository.findById(videoId);
        if(optional.isPresent()){
            Video video = optional.get();
            if(video.isUploaded()) {
                video.setStatus(Status.NOT_AVAILABLE);
                videoRepository.save(video);
                try {
                    Process process = Runtime.getRuntime().exec("rm /storage/var/video/" + video.getId() + " -r");
                    int statusCode = process.waitFor();
                    System.out.println("Process exit code: " + statusCode);
                    System.out.println("Result:");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    line = "";
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    process = Runtime.getRuntime().exec("rm /storage/var/videofiles/" + video.getId() + " -r");
                    statusCode = process.waitFor();
                    System.out.println("Process exit code: " + statusCode);
                    System.out.println("Result:");
                    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    line = "";
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    line = "";
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}
